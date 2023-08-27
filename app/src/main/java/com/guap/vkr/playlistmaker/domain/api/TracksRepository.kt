package com.guap.vkr.playlistmaker.domain.api

import com.guap.vkr.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}