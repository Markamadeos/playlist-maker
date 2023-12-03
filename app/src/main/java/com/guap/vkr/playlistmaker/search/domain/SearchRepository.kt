package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTrack(expression: String): Flow<ResponseStatus<List<Track>>>
    fun getTrackHistoryList(): List<Track>
    fun addTrackInHistory(track: Track)
    fun clearHistory()
}