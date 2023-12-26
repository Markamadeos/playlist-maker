package com.guap.vkr.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.LibraryInteractor
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val track: Track,
    private val favoriteTracksInteractor: LibraryInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var clickAllowed = true

    private val stateLiveData = MutableLiveData<MediaPlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    private val likeLiveData = MutableLiveData<Boolean>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData
    fun observeLike(): LiveData<Boolean> = likeLiveData

    init {
        renderState(MediaPlayerState.Default)
        prepareAudioPlayer()
        setOnCompleteListener()
        isClickAllowed()
        likeLiveData.postValue(track.isFavorite)
    }

    private fun prepareAudioPlayer() {
        mediaPlayerInteractor.preparePlayer(track.previewUrl) {
            renderState(MediaPlayerState.Prepared)
        }
    }

    private fun startAudioPlayer() {
        renderState(MediaPlayerState.Playing)
        mediaPlayerInteractor.startPlayer()
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        renderState(MediaPlayerState.Paused)
    }


    private fun getCurrentPlaybackPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            renderState(MediaPlayerState.Prepared)
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

    private fun renderState(state: MediaPlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        releaseResources()
    }

    fun onPause() {
        pauseAudioPlayer()
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }

}