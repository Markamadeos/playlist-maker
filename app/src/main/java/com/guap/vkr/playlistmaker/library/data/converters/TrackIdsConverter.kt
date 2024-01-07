package com.guap.vkr.playlistmaker.library.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackIdsConverter {

    @TypeConverter
    fun fromList(value: ArrayList<Long>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<Long>? {
        val itemType = object : TypeToken<ArrayList<Long>>() {}.type
        return Gson().fromJson<ArrayList<Long>>(value, itemType)
    }
}