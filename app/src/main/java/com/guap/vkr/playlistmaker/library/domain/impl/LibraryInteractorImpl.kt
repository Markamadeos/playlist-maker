package com.guap.vkr.playlistmaker.library.domain.impl

import com.guap.vkr.playlistmaker.library.domain.api.LibraryInteractor
import com.guap.vkr.playlistmaker.library.domain.api.LibraryRepository
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class LibraryInteractorImpl(private val libraryRepository: LibraryRepository) : LibraryInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        libraryRepository.addTrackToFavorites(track = track)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        libraryRepository.deleteTrackFromFavorites(track = track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return libraryRepository.getFavoriteTracks()
    }


//    override fun getFavoriteTracksIds(): Flow<List<Long>> {
//        return libraryRepository.getFavoriteTracksIds()
//    }
}