package com.guap.vkr.playlistmaker.search.data

import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.data.dto.TrackDto
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchRequest
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchResponse
import com.guap.vkr.playlistmaker.search.domain.SearchRepository
import com.guap.vkr.playlistmaker.search.domain.model.TrackSearchModel
import javax.net.ssl.HttpsURLConnection

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dataStorageSearchFeature: DataStorageSearchFeature
) : SearchRepository {

    override fun searchTrack(expression: String): ResponseStatus<List<TrackSearchModel>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                ResponseStatus.Error(true)
            }

            HttpsURLConnection.HTTP_OK -> {
                ResponseStatus.Success((response as TracksSearchResponse).results.map {
                    TrackSearchModel(
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
                })
            }

            else -> {
                ResponseStatus.Error(true)
            }
        }
    }

    override fun getTrackHistoryList(): List<TrackSearchModel> {
        return dataStorageSearchFeature.getSearchHistory().map {
            TrackSearchModel(
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
    }

    override fun addTrackInHistory(track: TrackSearchModel) {
        dataStorageSearchFeature.addTrackToHistory(
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
                track.previewUrl
            )
        )
    }

    override fun clearHistory() {
        dataStorageSearchFeature.clearHistory()
    }
}