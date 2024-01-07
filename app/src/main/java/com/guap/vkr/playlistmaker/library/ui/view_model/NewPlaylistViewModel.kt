package com.guap.vkr.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(playlistName: String, playlistDescription: String, cover: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                playlist = Playlist(
                    playlistId = null,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    imgUri = saveCover(cover),
                    tracks = arrayListOf()
                )
            )
        }
    }

    private fun saveCover(uri: Uri?): String? {
        return if (uri != Uri.EMPTY) {
            val fileName = System.currentTimeMillis().toString()
            playlistInteractor.saveFile(uri = uri.toString(), fileName = fileName)
            getCover(fileName)
        } else {
            null
        }
    }

    private fun getCover(fileName: String): String {
        return playlistInteractor.getFile(fileName = fileName).toString()
    }
}