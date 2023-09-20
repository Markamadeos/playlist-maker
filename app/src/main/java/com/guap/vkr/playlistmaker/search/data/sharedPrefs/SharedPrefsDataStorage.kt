package com.guap.vkr.playlistmaker.search.data.sharedPrefs

import android.app.Application.MODE_PRIVATE
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guap.vkr.playlistmaker.search.data.DataStorage
import com.guap.vkr.playlistmaker.search.data.dto.TrackDto
import com.guap.vkr.playlistmaker.utils.SEARCH_HISTORY_KEY
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import java.lang.reflect.Type

class SharedPrefsDataStorage(context: Context) : DataStorage {

    private val sharedPref = context.getSharedPreferences(
        SHARED_PREFERENCES,
        MODE_PRIVATE
    )

    private val historyList = readFromSharedPref()

    override fun getSearchHistory() = historyList

    override fun clearHistory() {
        historyList.clear()
        updateSharedPref()
    }

    override fun addTrackToHistory(track: TrackDto) {
        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        if (historyList.size == HISTORY_LIST_SIZE) {
            historyList.removeLast()
        }
        historyList.add(FIRST, track)
        updateSharedPref()
    }

    private fun updateSharedPref() {
        sharedPref.edit().clear().putString(SEARCH_HISTORY_KEY, Gson().toJson(historyList)).apply()
    }

    private fun readFromSharedPref(): ArrayList<TrackDto> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<TrackDto?>?>() {}.type
        return Gson().fromJson<Any>(json, type) as ArrayList<TrackDto>
    }

    companion object {
        private const val HISTORY_LIST_SIZE = 10
        private const val FIRST = 0
    }

}