package com.guap.vkr.playlistmaker.data.sharedPref

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guap.vkr.playlistmaker.domain.models.Track
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

    private fun readFromSharedPref(): ArrayList<Track> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        return Gson().fromJson<Any>(json, type) as ArrayList<Track>
    }

    companion object {
        private const val HISTORY_LIST_SIZE = 10
        private const val FIRST = 0
    }
}