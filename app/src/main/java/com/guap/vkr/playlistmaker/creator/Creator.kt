package com.guap.vkr.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.guap.vkr.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.player.domain.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.settings.data.DataStorage
import com.guap.vkr.playlistmaker.settings.data.SettingsRepositoryImpl
import com.guap.vkr.playlistmaker.settings.data.impl.SharedPrefDataStorage
import com.guap.vkr.playlistmaker.settings.domain.SettingsInteractor
import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.guap.vkr.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor
import com.guap.vkr.playlistmaker.sharing.domain.impl.SharingIntercatorImpl

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractor(MediaPlayerRepositoryImpl())
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(SettingsRepositoryImpl(provideDataStorage(context)))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingIntercatorImpl(ExternalNavigatorImpl(context))
    }

    private fun provideDataStorage(context: Context): DataStorage {
        return SharedPrefDataStorage(context)
    }

}
