package com.guap.vkr.playlistmaker.library.domain.impl

import com.guap.vkr.playlistmaker.di.repositoryModule
import com.guap.vkr.playlistmaker.library.domain.api.InternalStorageRepository
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistInteractor
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistRepository
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
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

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }

    override suspend fun getTracks(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getTracks(playlistId = playlistId)
    }

    override suspend fun deleteTrack(track: Track, playlistId: Long) {
        playlistRepository.deleteTrack(track = track, playlistId = playlistId)
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(playlistId = playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist = playlist)
    }
}