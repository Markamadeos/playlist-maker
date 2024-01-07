package com.guap.vkr.playlistmaker.library.domain.model

data class Playlist(
    val playlistId: Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val imgUri: String?,
    val trackIds: ArrayList<Long> = arrayListOf(),
    var tracksCount: Int = 0
)
