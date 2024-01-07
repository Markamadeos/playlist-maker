package com.guap.vkr.playlistmaker.library.domain.model

import com.guap.vkr.playlistmaker.search.domain.model.Track

data class Playlist(
    val playlistId: Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val imgUri: String?,
    val tracks: ArrayList<Track> = arrayListOf(),
    var tracksCount: Int = 0
)
