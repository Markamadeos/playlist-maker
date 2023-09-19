package com.guap.vkr.playlistmaker.settings.data

import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val dataStorage: DataStorage) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return dataStorage.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        dataStorage.saveTheme(settings)
    }
}