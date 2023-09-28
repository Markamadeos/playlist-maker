package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(mediaPlayerRepository = get())
    }
}