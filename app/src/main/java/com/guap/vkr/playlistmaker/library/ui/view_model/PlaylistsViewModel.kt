package com.guap.vkr.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.PlaylistsState
import kotlinx.coroutines.launch


class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _stateLiveDataL = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = _stateLiveDataL

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isNotEmpty()) {
            renderState(PlaylistsState.StateContent(playlists))
        } else {
            renderState(PlaylistsState.StateEmpty)
        }
    }

    private fun renderState(state: PlaylistsState) {
        _stateLiveDataL.postValue(state)
    }
}