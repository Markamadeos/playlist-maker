package com.guap.vkr.playlistmaker.search.domain.api

import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<List<Track>?>
    fun getTracksHistory(): Flow<List<Track>?>

    fun addTrackToHistory(track: Track)
    fun clearHistory()
}