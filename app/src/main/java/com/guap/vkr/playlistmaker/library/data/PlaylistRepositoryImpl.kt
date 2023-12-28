package com.guap.vkr.playlistmaker.library.data

import com.guap.vkr.playlistmaker.library.data.converters.PlaylistDbConverter
import com.guap.vkr.playlistmaker.library.data.db.AppDatabase
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistEntity
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist = playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist = playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getPlaylists()
            emit(convertFromPlaylistEntity(playlists))
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

}