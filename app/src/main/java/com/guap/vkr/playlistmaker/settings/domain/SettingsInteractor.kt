package com.guap.vkr.playlistmaker.settings.domain

import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings

    fun updateThemeSetting(settings: ThemeSettings)
}