package com.guap.vkr.playlistmaker.library.domain.api

import com.guap.vkr.playlistmaker.library.domain.model.TrackLibraryModel
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    suspend fun addTrackToFavorites(track: TrackLibraryModel)

    suspend fun deleteTrackFromFavorites(track: TrackLibraryModel)

    fun getFavoriteTracks(): Flow<List<TrackLibraryModel>>

}