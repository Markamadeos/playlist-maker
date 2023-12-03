package com.guap.vkr.playlistmaker.search.data

import com.guap.vkr.playlistmaker.library.data.db.AppDatabase
import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.data.dto.TrackDto
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchRequest
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchResponse
import com.guap.vkr.playlistmaker.search.domain.api.SearchRepository
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.net.ssl.HttpsURLConnection

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchDataStorage: SearchDataStorage,
    private val appDatabase: AppDatabase
) : SearchRepository {

    override fun searchTrack(expression: String): Flow<ResponseStatus<List<Track>>> =
        flow {

            val response = networkClient.doRequest(TracksSearchRequest(expression))

            when (response.resultCode) {
                -1 -> {
                    emit(ResponseStatus.Error())
                }

                HttpsURLConnection.HTTP_OK -> {
                    with(response as TracksSearchResponse) {
                        val data = results.map {
                            Track(
                                it.trackId,
                                it.trackName,
                                it.artistName,
                                it.trackTimeMillis,
                                it.artworkUrl100,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.previewUrl
                            )
                        }
                        getFavoriteTracksIds().map { favoriteTracksIds ->
                            data.forEach { track ->
                                if (favoriteTracksIds.contains(track.trackId)) {
                                    track.isFavorite = true
                                }
                            }
                        }
                        emit(ResponseStatus.Success(data = data))
                    }
                }

                else -> {
                    emit(ResponseStatus.Error())
                }
            }
        }


    override fun getTrackHistoryList(): List<Track> {
        val tracksHistory = searchDataStorage.getSearchHistory()

        getFavoriteTracksIds().map { favoriteTracksIds ->
            tracksHistory.forEach { track ->
                if (favoriteTracksIds.contains(track.trackId)) {
                    track.isFavorite = true
                }
            }
        }

        return tracksHistory.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl,
                it.isFavorite
            )
        }
    }

    override fun addTrackInHistory(track: Track) {
        searchDataStorage.addTrackToHistory(
            TrackDto(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
                track.isFavorite
            )
        )
    }

    override fun clearHistory() {
        searchDataStorage.clearHistory()
    }

    override fun getFavoriteTracksIds(): Flow<List<Long>> {
        return flow {
            val ids = appDatabase.trackDao().getTracksIds()
            emit(ids)
        }
    }
}