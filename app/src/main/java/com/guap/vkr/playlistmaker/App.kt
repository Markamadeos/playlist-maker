package com.guap.vkr.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        if(sharedPref.contains(THEME_SWITCH_KEY)) {
            darkTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
            switchTheme(darkTheme)
        } else {
            darkTheme = false
        }

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPref.edit().putBoolean(THEME_SWITCH_KEY, darkThemeEnabled).apply()
    }
}

