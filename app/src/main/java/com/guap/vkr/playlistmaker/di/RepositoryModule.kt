package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.library.data.FavoritesRepositoryImpl
import com.guap.vkr.playlistmaker.library.data.converters.TrackDbConverter
import com.guap.vkr.playlistmaker.library.domain.api.FavoritesRepository
import com.guap.vkr.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerRepository
import com.guap.vkr.playlistmaker.search.data.SearchRepositoryImpl
import com.guap.vkr.playlistmaker.search.domain.api.SearchRepository
import com.guap.vkr.playlistmaker.settings.data.SettingsRepositoryImpl
import com.guap.vkr.playlistmaker.settings.domain.SettingsRepository
import com.guap.vkr.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.guap.vkr.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(audioPlayer = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(settingsDataStorage = get(), applicationContext = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), searchDataStorage = get(), appDatabase = get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(appDatabase = get(), trackDbConverter = get())
    }

    factory { TrackDbConverter() }
}