package com.guap.vkr.playlistmaker.data

import com.guap.vkr.playlistmaker.data.dto.TracksSearchRequest
import com.guap.vkr.playlistmaker.data.dto.TracksSearchResponse
import com.guap.vkr.playlistmaker.domain.api.TracksRepository
import com.guap.vkr.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == RESPONSE_OK) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.getDuration(),
                    it.artworkUrl100,
                    it.collectionName,
                    it.getReleaseYear(),
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }

    companion object {
        private const val RESPONSE_OK = 200
    }
}