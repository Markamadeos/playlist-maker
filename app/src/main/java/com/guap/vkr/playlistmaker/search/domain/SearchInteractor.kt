package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<List<Track>?>
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: Track)
    fun clearHistory()

    interface HistoryConsumer {
        fun consume(tracks: List<Track>?)
    }
}