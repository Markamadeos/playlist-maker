package com.guap.vkr.playlistmaker.settings.data

import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val dataStorageSettingsFeature: DataStorageSettingsFeature) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return dataStorageSettingsFeature.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        dataStorageSettingsFeature.saveTheme(settings)
    }
}