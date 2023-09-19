package com.guap.vkr.playlistmaker

import com.guap.vkr.playlistmaker.player.domain.model.Track

class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)