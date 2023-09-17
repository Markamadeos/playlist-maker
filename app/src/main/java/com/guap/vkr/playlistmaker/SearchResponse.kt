package com.guap.vkr.playlistmaker

import com.guap.vkr.playlistmaker.domain.player.model.Track

class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)