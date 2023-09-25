package com.guap.vkr.playlistmaker.search.domain

import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.domain.model.TrackSearchModel

interface SearchRepository {
    fun searchTrack(expression: String): ResponseStatus<List<TrackSearchModel>>
    fun getTrackHistoryList(): List<TrackSearchModel>
    fun addTrackInHistory(track: TrackSearchModel)
    fun clearHistory()
}