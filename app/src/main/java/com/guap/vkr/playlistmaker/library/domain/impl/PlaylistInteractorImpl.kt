package com.guap.vkr.playlistmaker.library.domain.impl

import com.guap.vkr.playlistmaker.library.domain.api.InternalStorageRepository
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistRepository
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val internalStorageRepository: InternalStorageRepository
) :
    PlaylistInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist = playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        updatePlaylist(playlist = playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun saveFile(uri: String, fileName: String) {
        internalStorageRepository.saveFile(uri = uri, fileName = fileName)
    }

    override fun getFile(fileName: String): String {
        return internalStorageRepository.getFile(fileName = fileName)
    }
}