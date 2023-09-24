package com.guap.vkr.playlistmaker.settings.data

import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings

interface DataStorageSettingsFeature {

    fun saveTheme(themeSettings: ThemeSettings)

    fun getTheme(): ThemeSettings

}