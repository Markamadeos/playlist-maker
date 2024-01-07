package com.guap.vkr.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guap.vkr.playlistmaker.search.domain.model.Track

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long?,
    val playlistName: String,
    val playlistDescription: String?,
    val imgUri: String?,
    val tracks: ArrayList<Track> = arrayListOf(),
    val tracksCount: Int = 0
)