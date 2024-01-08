package com.guap.vkr.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailState
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistId: Long
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistDetailState>()
    fun observeState(): LiveData<PlaylistDetailState> = stateLiveData

    private fun renderPlaylistDetailState(state: PlaylistDetailState) {
        stateLiveData.postValue(state)
    }

    fun updateData() {
        var tracks: List<Track> = listOf()
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getTracks(playlistId)
                .collect {
                    tracks = it
                }
            playlistInteractor.getPlaylistById(playlistId).collect {
                processResult(it, tracks)
            }
        }
    }

    private fun processResult(playlist: Playlist, tracks: List<Track>) {
        when {
            tracks.isEmpty() -> {
                renderPlaylistDetailState(PlaylistDetailState.Empty(playlist))
            }

            else -> {
                renderPlaylistDetailState(
                    PlaylistDetailState.Content(
                        tracks = tracks,
                        playlist = playlist,
                        duration = getDuration(tracks)
                    )
                )
            }
        }
    }

    private fun getDuration(tracks: List<Track>): Long {
        var duration: Long = 0L
        tracks.map {
            duration += it.trackTimeMillis
        }
        return duration
    }

    fun deleteTrack(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrack(track = track, playlistId = playlistId)
            updateData()
        }
    }

}