package com.guap.vkr.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guap.vkr.playlistmaker.library.data.db.dao.PlaylistDao
import com.guap.vkr.playlistmaker.library.data.db.dao.TrackDao
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistEntity
import com.guap.vkr.playlistmaker.library.data.db.entity.FavoriteTrackEntity
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}