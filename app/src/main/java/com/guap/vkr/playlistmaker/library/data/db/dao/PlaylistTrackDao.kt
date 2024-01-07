package com.guap.vkr.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks_table")
    suspend fun getTracks(): List<PlaylistTrackEntity>

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)

}