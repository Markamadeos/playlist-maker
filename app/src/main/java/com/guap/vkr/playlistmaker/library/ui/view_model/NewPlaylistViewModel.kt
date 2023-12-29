package com.guap.vkr.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch
import java.io.File

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(playlist = playlist)
        }
    }

    // returns filename
    fun saveCover(uri: Uri?): String {
        val fileName = System.currentTimeMillis().toString()
        playlistInteractor.saveFile(uri = uri.toString(), fileName = fileName)
        return fileName
    }

    fun getCover(fileName: String): File {
        return playlistInteractor.getFile(fileName = fileName)
    }
}