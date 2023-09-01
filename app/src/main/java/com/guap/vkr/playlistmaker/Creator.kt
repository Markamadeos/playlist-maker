package com.guap.vkr.playlistmaker

import com.guap.vkr.playlistmaker.data.MediaPlayerRepositoryImpl
import com.guap.vkr.playlistmaker.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.domain.impl.MediaPlayerInteractorImpl

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }
}
