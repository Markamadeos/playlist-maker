package com.guap.vkr.playlistmaker.search.data

import com.guap.vkr.playlistmaker.search.data.dto.TrackDto


interface SearchDataStorage {
    fun getSearchHistory(): ArrayList<TrackDto>
    fun clearHistory()
    fun addTrackToHistory(track: TrackDto)
}