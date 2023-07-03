package com.guap.vkr.playlistmaker.api

import com.guap.vkr.playlistmaker.model.Track

class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)