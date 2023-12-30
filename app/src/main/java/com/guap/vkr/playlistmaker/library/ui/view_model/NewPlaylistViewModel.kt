package com.guap.vkr.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(playlist = playlist)
        }
    }

    fun saveCover(uri: Uri?): String {
        val fileName = System.currentTimeMillis().toString()
        playlistInteractor.saveFile(uri = uri.toString(), fileName = fileName)
        return getCover(fileName)
    }

    private fun getCover(fileName: String): String {
        return playlistInteractor.getFile(fileName = fileName).toString()
    }
}