package com.guap.vkr.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.guap.vkr.playlistmaker.settings.data.DataStorage
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import com.guap.vkr.playlistmaker.utils.THEME_SWITCH_KEY

class SharedPrefDataStorage(context: Context) : DataStorage {

    private val sharedPrefs = context.getSharedPreferences(
        SHARED_PREFERENCES,
        MODE_PRIVATE
    )

    override fun saveTheme(themeSettings: ThemeSettings) {
        sharedPrefs.edit().putBoolean(THEME_SWITCH_KEY, themeSettings.isDark).apply()
    }

    override fun getTheme(): ThemeSettings {
        return ThemeSettings(isDark = sharedPrefs.getBoolean(THEME_SWITCH_KEY, false))
    }

}
