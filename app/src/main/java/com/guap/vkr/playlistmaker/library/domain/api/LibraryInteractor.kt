package com.guap.vkr.playlistmaker.library.domain.api

import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryInteractor {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun deleteTrackFromFavorites(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    fun getFavoriteTracksIds(): Flow<List<Long>>

}