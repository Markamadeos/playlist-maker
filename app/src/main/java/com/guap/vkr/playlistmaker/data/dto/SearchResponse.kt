package com.guap.vkr.playlistmaker.data.dto

import com.guap.vkr.playlistmaker.domain.models.Track

class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)