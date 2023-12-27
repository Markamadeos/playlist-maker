package com.guap.vkr.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guap.vkr.playlistmaker.library.data.db.dao.PlaylistDao
import com.guap.vkr.playlistmaker.library.data.db.dao.TrackDao
import com.guap.vkr.playlistmaker.library.data.db.entity.PlaylistEntity
import com.guap.vkr.playlistmaker.library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}