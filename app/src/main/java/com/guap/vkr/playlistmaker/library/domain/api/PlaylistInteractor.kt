package com.guap.vkr.playlistmaker.library.domain.api

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun saveFile(uri: String, fileName: String)

    fun getFile(fileName: String): String

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun getTracks(playlist: Playlist): Flow<List<Track>>

    suspend fun deleteTrack(track: Track)

}