package com.guap.vkr.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.utils.App
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.settings.domain.SettingsInteractor
import com.guap.vkr.playlistmaker.settings.domain.model.ThemeSettings
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val app: App
) : ViewModel() {

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
        app.switchTheme(checked)
    }

    companion object {

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(
                    settingsInteractor = Creator.provideSettingsInteractor(context),
                    sharingInteractor = Creator.provideSharingInteractor(context),
                    app = app
                )
            }
        }
    }
}