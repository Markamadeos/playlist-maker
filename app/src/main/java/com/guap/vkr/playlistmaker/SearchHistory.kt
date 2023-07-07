package com.guap.vkr.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guap.vkr.playlistmaker.model.Track
import com.guap.vkr.playlistmaker.utils.SEARCH_HISTORY_KEY
import java.lang.reflect.Type

class SearchHistory(private val sharedPref: SharedPreferences) {

    private val historyList = readFromSharedPref()

    fun getSearchHistory() = historyList

    fun clearHistory() {
        historyList.clear()
        updateSharedPref()
    }

    fun addTrackToHistory(track: Track) {
        historyList.add(0, track)
        updateSharedPref()
    }

    private fun updateSharedPref() {
        sharedPref.edit().clear().putString(SEARCH_HISTORY_KEY, Gson().toJson(historyList)).apply()
    }

    private fun readFromSharedPref(): ArrayList<Track> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        return Gson().fromJson<Any>(json, type) as ArrayList<Track>
    }
}