package com.guap.vkr.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guap.vkr.playlistmaker.settings.domain.SettingsInteractor
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
        private val sharingInteractor: SharingInteractor,
        private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var darkTheme = true
    private val _themeLivedata = MutableLiveData<Boolean>()
    val themeLiveData: LiveData<Boolean> = _themeLivedata

    init {
        _themeLivedata.value = (settingsInteractor.getThemeSettings().isDark)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun setupTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(checked))
        _themeLivedata.postValue(checked)
    }
}