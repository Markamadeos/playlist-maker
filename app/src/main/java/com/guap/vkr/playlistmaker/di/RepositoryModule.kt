package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(audioPlayer =  get())
    }
}