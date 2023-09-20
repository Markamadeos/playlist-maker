package com.guap.vkr.playlistmaker.search.domain.impl

import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.domain.SearchRepository
import com.guap.vkr.playlistmaker.search.domain.SearchInteractor
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()


    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            when(val resource = repository.searchTrack(expression)) {
                is ResponseStatus.Success -> { consumer.consume(resource.data, false) }
                is ResponseStatus.Error -> { consumer.consume(null,  true) }
            }
        }
    }

    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}