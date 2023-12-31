package com.guap.vkr.playlistmaker.library.domain.impl

import com.guap.vkr.playlistmaker.library.domain.api.FavoritesInteractor
import com.guap.vkr.playlistmaker.library.domain.api.FavoritesRepository
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        favoritesRepository.addTrackToFavorites(track = track)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        favoritesRepository.deleteTrackFromFavorites(track = track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoriteTracks()
    }
}