package com.guap.vkr.playlistmaker.settings.data

import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val settingsDataStorage: SettingsDataStorage) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return settingsDataStorage.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsDataStorage.saveTheme(settings)
    }
}