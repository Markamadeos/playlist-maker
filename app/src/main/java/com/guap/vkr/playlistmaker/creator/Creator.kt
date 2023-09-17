package com.guap.vkr.playlistmaker.creator

import com.guap.vkr.playlistmaker.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.domain.player.MediaPlayerInteractor

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractor(MediaPlayerRepositoryImpl())
    }
}
