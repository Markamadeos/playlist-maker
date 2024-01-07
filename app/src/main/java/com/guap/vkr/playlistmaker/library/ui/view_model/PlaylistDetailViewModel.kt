package com.guap.vkr.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistDetailState

class PlaylistDetailViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : ViewModel() {

    fun initState() {
        if (playlist.trackIds.isNullOrEmpty()) {
            renderPlaylistDetailState(PlaylistDetailState.Empty)
        } else {
            renderPlaylistDetailState(PlaylistDetailState.Content(playlist))
        }
    }

    private val stateLiveData = MutableLiveData<PlaylistDetailState>()
    fun observeState(): LiveData<PlaylistDetailState> = stateLiveData

    private fun renderPlaylistDetailState(state: PlaylistDetailState) {
        stateLiveData.postValue(state)
    }

}