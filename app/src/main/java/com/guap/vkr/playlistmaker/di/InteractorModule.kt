package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.guap.vkr.playlistmaker.search.domain.SearchInteractor
import com.guap.vkr.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.guap.vkr.playlistmaker.settings.domain.SettingsInteractor
import com.guap.vkr.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor
import com.guap.vkr.playlistmaker.sharing.domain.impl.SharingIntercatorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(mediaPlayerRepository = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    single<SharingInteractor> {
       SharingIntercatorImpl(externalNavigator = get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
}