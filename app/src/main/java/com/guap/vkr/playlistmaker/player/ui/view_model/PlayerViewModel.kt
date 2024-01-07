package com.guap.vkr.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.FavoritesInteractor
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.player.ui.model.PlayerBottomSheetState
import com.guap.vkr.playlistmaker.player.ui.model.TrackInPlaylistState
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val track: Track,
    private val favoriteTracksInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var clickAllowed = true

    private val stateLiveData = MutableLiveData<MediaPlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    private val likeLiveData = MutableLiveData<Boolean>()
    private val bottomSheetLiveData = MutableLiveData<PlayerBottomSheetState>()
    private val trackInPlaylistLiveData = MutableLiveData<TrackInPlaylistState>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData
    fun observeLike(): LiveData<Boolean> = likeLiveData
    fun observeBottomSheetState(): LiveData<PlayerBottomSheetState> = bottomSheetLiveData
    fun observeTrackInPlaylistState(): LiveData<TrackInPlaylistState> = trackInPlaylistLiveData

    init {
        renderPlayerScreenState(MediaPlayerState.Default)
        prepareAudioPlayer()
        setOnCompleteListener()
        isClickAllowed()
        likeLiveData.postValue(track.isFavorite)
        getPlaylists()
    }

    private fun prepareAudioPlayer() {
        mediaPlayerInteractor.preparePlayer(track.previewUrl) {
            renderPlayerScreenState(MediaPlayerState.Prepared)
        }
    }

    private fun startAudioPlayer() {
        renderPlayerScreenState(MediaPlayerState.Playing)
        mediaPlayerInteractor.startPlayer()
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        renderPlayerScreenState(MediaPlayerState.Paused)
    }


    private fun getCurrentPlaybackPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            renderPlayerScreenState(MediaPlayerState.Prepared)
        }
        timerJob?.cancel()
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is MediaPlayerState.Playing -> {
                pauseAudioPlayer()
            }

            is MediaPlayerState.Prepared, MediaPlayerState.Paused -> {
                startAudioPlayer()
            }

            else -> {}
        }
        updateTimer()
    }

    private fun renderPlayerScreenState(state: MediaPlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        releaseResources()
    }

    fun onPause() {
        if (stateLiveData.value is MediaPlayerState.Playing) {
            pauseAudioPlayer()
        }
        timerJob?.cancel()
    }

    private fun updateTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                timerLiveData.postValue(getCurrentPlaybackPosition())
                delay(PLAYBACK_UPDATE_DELAY_MS)
            }
        }
    }

    fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MS)
                clickAllowed = true
            }
        }
        return current
    }

    fun isLikeButtonClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTracksInteractor.deleteTrackFromFavorites(track)
                track.isFavorite = false
            } else {
                favoriteTracksInteractor.addTrackToFavorites(track)
                track.isFavorite = true
            }
            likeLiveData.postValue(track.isFavorite)
        }
    }

    fun releaseResources() {
        mediaPlayerInteractor.destroyPlayer()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        when {
            playlists.isEmpty() -> {
                renderBottomSheetState(PlayerBottomSheetState.Empty)
            }

            playlists.isNotEmpty() -> {
                renderBottomSheetState(PlayerBottomSheetState.Content(playlists))
            }
        }
    }

    private fun renderBottomSheetState(state: PlayerBottomSheetState) {
        bottomSheetLiveData.postValue(state)
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        if (playlist.tracks.contains(track)) {
            renderTrackToPlaylistState(TrackInPlaylistState.Exist(playlist))
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(playlist, track)
            }
            renderTrackToPlaylistState(TrackInPlaylistState.Added(playlist))
        }
    }

    private fun renderTrackToPlaylistState(state: TrackInPlaylistState) {
        trackInPlaylistLiveData.postValue(state)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }

}