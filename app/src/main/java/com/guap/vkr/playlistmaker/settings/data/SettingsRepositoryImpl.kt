package com.guap.vkr.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val settingsDataStorage: SettingsDataStorage) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return settingsDataStorage.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsDataStorage.saveTheme(settings)
    }
}