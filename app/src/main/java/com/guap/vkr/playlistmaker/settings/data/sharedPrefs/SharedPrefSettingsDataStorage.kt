package com.guap.vkr.playlistmaker.settings.data.sharedPrefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.guap.vkr.playlistmaker.settings.data.SettingsDataStorage
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import com.guap.vkr.playlistmaker.utils.THEME_SWITCH_KEY

class SharedPrefSettingsDataStorage(context: Context) : SettingsDataStorage {

    private val sharedPrefs = context.getSharedPreferences(
        SHARED_PREFERENCES,
        MODE_PRIVATE
    )

    private val darkThemeEnabled = sharedPrefs.getBoolean(THEME_SWITCH_KEY, false)

    override fun saveTheme(themeSettings: ThemeSettings) {
        sharedPrefs.edit().putBoolean(THEME_SWITCH_KEY, themeSettings.isDark).apply()
    }

    override fun getTheme(): ThemeSettings {
        return ThemeSettings(darkThemeEnabled)
    }
}
