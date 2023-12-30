package com.guap.vkr.playlistmaker.library.domain.api

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun saveFile(uri: String, fileName: String)

    fun getFile(fileName: String): String

}