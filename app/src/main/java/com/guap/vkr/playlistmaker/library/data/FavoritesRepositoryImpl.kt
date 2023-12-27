package com.guap.vkr.playlistmaker.library.data

import com.guap.vkr.playlistmaker.library.data.converters.TrackDbConverter
import com.guap.vkr.playlistmaker.library.data.db.AppDatabase
import com.guap.vkr.playlistmaker.library.data.db.entity.TrackEntity
import com.guap.vkr.playlistmaker.library.domain.api.FavoritesRepository
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(track = trackDbConverter.map(track))
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(track = trackDbConverter.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.trackDao().getTracks()
            emit(convertFromTrackEntity(tracks))
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}