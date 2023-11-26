package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.domain.model.TrackSearchModel
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<List<TrackSearchModel>?>
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: TrackSearchModel)
    fun clearHistory()

    interface HistoryConsumer {
        fun consume(tracks: List<TrackSearchModel>?)
    }
}