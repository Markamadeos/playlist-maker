package com.guap.vkr.playlistmaker.creator

import android.content.Context
import com.guap.vkr.playlistmaker.search.data.SearchRepositoryImpl
import com.guap.vkr.playlistmaker.search.data.network.RetrofitNetworkClient
import com.guap.vkr.playlistmaker.search.domain.SearchInteractor
import com.guap.vkr.playlistmaker.search.domain.SearchRepository
import com.guap.vkr.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.guap.vkr.playlistmaker.settings.data.SettingsDataStorage
import com.guap.vkr.playlistmaker.settings.data.SettingsRepositoryImpl
import com.guap.vkr.playlistmaker.settings.data.sharedPrefs.SharedPrefSettingsDataStorage
import com.guap.vkr.playlistmaker.settings.domain.SettingsInteractor
import com.guap.vkr.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.guap.vkr.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor
import com.guap.vkr.playlistmaker.sharing.domain.impl.SharingIntercatorImpl

object Creator {

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(SettingsRepositoryImpl(provideDataStorage(context)))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingIntercatorImpl(ExternalNavigatorImpl(context))
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository(context))
    }

    private fun provideDataStorage(context: Context): SettingsDataStorage {
        return SharedPrefSettingsDataStorage(context)
    }

    private fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
                RetrofitNetworkClient(context),
                com.guap.vkr.playlistmaker.search.data.sharedPrefs.SharedPrefsSearchDataStorage(context),
        )
    }
}
