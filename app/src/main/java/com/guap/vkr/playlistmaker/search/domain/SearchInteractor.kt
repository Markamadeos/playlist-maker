package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.domain.model.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: Track)
    fun clearHistory()

    interface SearchConsumer {
        fun consume(tracks: List<Track>?, hasError: Boolean?)
    }

    interface HistoryConsumer {
        fun consume(tracks: List<Track>?)
    }
}