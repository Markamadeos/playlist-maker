package com.guap.vkr.playlistmaker

import com.guap.vkr.playlistmaker.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.domain.MediaPlayerInteractor

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractor(MediaPlayerRepositoryImpl())
    }
}
