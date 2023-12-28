package com.guap.vkr.playlistmaker.library.domain

data class Playlist(
    val playlistId: Long,
    val playlistName: String,
    val playlistDescription: String,
    val imgUri: String,
    val tracks: String,
    val tracksCount: Int
)
