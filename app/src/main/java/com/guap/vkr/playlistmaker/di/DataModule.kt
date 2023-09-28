package com.guap.vkr.playlistmaker.di

import android.media.MediaPlayer
import org.koin.dsl.module

val dataModule = module {
    factory {
        MediaPlayer()
    }
}