package com.guap.vkr.playlistmaker.settings.data

import android.content.Context
import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings
import com.guap.vkr.playlistmaker.app.App

class SettingsRepositoryImpl(
    private val settingsDataStorage: SettingsDataStorage,
    private val applicationContext: Context
) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return settingsDataStorage.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        (applicationContext as App).switchTheme(settings.isDark)
        settingsDataStorage.saveTheme(settings)
    }
}