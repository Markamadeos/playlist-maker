package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.domain.model.TrackSearchModel

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: TrackSearchModel)
    fun clearHistory()

    interface SearchConsumer {
        fun consume(tracks: List<TrackSearchModel>?, hasError: Boolean?)
    }

    interface HistoryConsumer {
        fun consume(tracks: List<TrackSearchModel>?)
    }
}