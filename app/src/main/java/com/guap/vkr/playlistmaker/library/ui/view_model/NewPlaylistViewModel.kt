package com.guap.vkr.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.model.EditPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private lateinit var playlist: Playlist

    fun createPlaylist(playlistName: String, playlistDescription: String, cover: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                playlist = Playlist(
                    playlistId = null,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    imgUri = saveCover(cover),
                    trackIds = arrayListOf()
                )
            )
        }
    }

    fun updatePlaylist(
        playlistId: Long,
        playlistName: String,
        playlistDescription: String,
        cover: Uri?
    ) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(
                playlist = Playlist(
                    playlistId = playlistId,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    imgUri = if (cover != Uri.EMPTY) {
                        saveCover(cover)
                    } else playlist.imgUri,
                    trackIds = playlist.trackIds
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

    private val stateLiveData = MutableLiveData<EditPlaylistState>()
    fun observeState(): LiveData<EditPlaylistState> = stateLiveData

    fun updateData(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(playlistId).collect {
                processResult(it)
                playlist = it
            }
        }
    }

    private fun processResult(playlist: Playlist) {
        stateLiveData.postValue(EditPlaylistState.FilledPlaylistData(playlist))
    }
}