package com.guap.vkr.playlistmaker.library.domain.api

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun getTracks(playlistId: Long): Flow<List<Track>>

    suspend fun deleteTrack(track: Track, playlistId: Long)

    suspend fun getPlaylistById(playlistId: Long): Flow<Playlist>

    suspend fun deletePlaylist(playlist: Playlist)

}