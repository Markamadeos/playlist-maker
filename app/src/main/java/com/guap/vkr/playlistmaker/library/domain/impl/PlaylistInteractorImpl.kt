package com.guap.vkr.playlistmaker.library.domain.impl

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
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
}