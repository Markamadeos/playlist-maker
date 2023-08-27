package com.guap.vkr.playlistmaker.domain.impl

import com.bumptech.glide.util.Executors
import com.guap.vkr.playlistmaker.domain.api.TracksInteractor
import com.guap.vkr.playlistmaker.domain.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.mainThreadExecutor()
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}