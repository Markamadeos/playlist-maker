package com.guap.vkr.playlistmaker

import com.guap.vkr.playlistmaker.data.TracksRepositoryImpl
import com.guap.vkr.playlistmaker.data.network.RetrofitNetworkClient
import com.guap.vkr.playlistmaker.domain.api.TracksInteractor
import com.guap.vkr.playlistmaker.domain.api.TracksRepository
import com.guap.vkr.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}