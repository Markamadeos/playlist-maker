package com.guap.vkr.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guap.vkr.playlistmaker.library.data.db.entity.FavoriteTrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table ORDER BY addedToFavorite DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_track_table")
    suspend fun getTracksIds(): List<Long>

}