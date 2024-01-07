package com.guap.vkr.playlistmaker.library.data

import com.guap.vkr.playlistmaker.library.data.converters.PlaylistDbConverter
import com.guap.vkr.playlistmaker.library.data.converters.PlaylistTrackDbConverter
import com.guap.vkr.playlistmaker.library.data.db.AppDatabase
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistEntity
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.guap.vkr.playlistmaker.library.domain.api.PlaylistRepository
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: PlaylistTrackDbConverter
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

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        appDatabase.playlistTrackDao().insertTrack(trackDbConverter.map(track))
        val playlistUpdated = playlist.apply {
            trackIds.add(FIRST, track.trackId)
            tracksCount++
        }
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlistUpdated))
    }

    override suspend fun getTracks(playlist: Playlist): Flow<List<Track>> {
        return flow {
            val tracks = convertFromTrackEntity(appDatabase.playlistTrackDao().getTracks())
            emit(
                tracks.filter {
                    playlist.trackIds.contains(it.trackId)
                }.sortedBy { track ->
                    playlist.trackIds.indexOf(track.trackId)
                }
            )
        }
    }

    override suspend fun deleteTrack(track: Track, playlist: Playlist) {
        val playlistUpdated = playlist.apply {
            trackIds.remove(track.trackId)
            tracksCount--
        }
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlistUpdated))
        var playlistsContainsTrack = false
        appDatabase.playlistDao().getPlaylists().map {
            if (it.trackIds.contains(track.trackId)) {
                playlistsContainsTrack = true
            }
        }
        if (!playlistsContainsTrack) {
            appDatabase.playlistTrackDao().deleteTrack(trackDbConverter.map(track))
        }
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return flow { appDatabase.playlistDao().getPlaytlistById(playlistId) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    private fun convertFromTrackEntity(tracks: List<PlaylistTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    companion object {
        private const val FIRST = 0
    }

}