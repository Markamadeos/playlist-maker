package com.guap.vkr.playlistmaker.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.guap.vkr.playlistmaker.di.dataModule
import com.guap.vkr.playlistmaker.di.interactorModule
import com.guap.vkr.playlistmaker.di.repositoryModule
import com.guap.vkr.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        initTheme()
    }

    private fun initTheme() {
        val sharedPref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        if (sharedPref.contains(THEME_SWITCH_KEY)) {
            darkTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
            switchTheme(darkTheme)
        } else {
            darkTheme = false
        }
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}


