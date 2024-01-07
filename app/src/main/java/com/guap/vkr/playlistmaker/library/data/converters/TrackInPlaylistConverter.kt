package com.guap.vkr.playlistmaker.library.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guap.vkr.playlistmaker.search.domain.model.Track

class TrackInPlaylistConverter {

    @TypeConverter
    fun fromList(value: ArrayList<Track>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<Track>? {
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson<ArrayList<Track>>(value, itemType)
    }
}