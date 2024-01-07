package com.guap.vkr.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailState
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistDetailState>()
    fun observeState(): LiveData<PlaylistDetailState> = stateLiveData

    private fun renderPlaylistDetailState(state: PlaylistDetailState) {
        stateLiveData.postValue(state)
    }

    fun getTracks() {
        viewModelScope.launch {
            playlistInteractor.getTracks(playlist)
                .collect {
                    processResult(it)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        when {
            tracks.isEmpty() -> {
                renderPlaylistDetailState(PlaylistDetailState.Empty)
            }

            else -> {
                renderPlaylistDetailState(
                    PlaylistDetailState.Content(
                        tracks = tracks,
                        playlist = playlist
                    )
                )
            }
        }
    }

    fun deleteTrack(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deleteTrack(track = track, playlist = playlist)
        }
        renderPlaylistDetailState(PlaylistDetailState.TrackDeleted)
    }

}