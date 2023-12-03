package com.guap.vkr.playlistmaker.library.data

import com.guap.vkr.playlistmaker.library.data.converters.TrackDbConverter
import com.guap.vkr.playlistmaker.library.data.db.AppDatabase
import com.guap.vkr.playlistmaker.library.data.db.entity.TrackEntity
import com.guap.vkr.playlistmaker.library.domain.api.LibraryRepository
import com.guap.vkr.playlistmaker.library.domain.model.TrackLibraryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class LibraryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : LibraryRepository {

    override suspend fun addTrackToFavorites(track: TrackLibraryModel) {
        appDatabase.trackDao().insertTrack(track = trackDbConverter.map(track))
    }

    override suspend fun deleteTrackFromFavorites(track: TrackLibraryModel) {
        appDatabase.trackDao().deleteTrack(track = trackDbConverter.map(track))
    }

    override fun getFavoriteTracks(): Flow<List<TrackLibraryModel>> {
        return flow {
            val tracks = appDatabase.trackDao().getTracks()
            emit(convertFromTrackEntity(tracks))
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackLibraryModel> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}