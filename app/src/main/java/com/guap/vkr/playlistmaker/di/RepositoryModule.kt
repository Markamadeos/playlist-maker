package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerRepository
import com.guap.vkr.playlistmaker.search.data.SearchRepositoryImpl
import com.guap.vkr.playlistmaker.search.domain.SearchRepository
import com.guap.vkr.playlistmaker.settings.data.SettingsRepositoryImpl
import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.guap.vkr.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(audioPlayer = get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(settingsDataStorage = get(), applicationContext = get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), searchDataStorage = get())
    }
}