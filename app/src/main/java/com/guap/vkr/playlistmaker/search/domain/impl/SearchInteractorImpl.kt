package com.guap.vkr.playlistmaker.search.domain.impl

import com.guap.vkr.playlistmaker.search.data.dto.ResponseStatus
import com.guap.vkr.playlistmaker.search.domain.api.SearchInteractor
import com.guap.vkr.playlistmaker.search.domain.api.SearchRepository
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun searchTracks(expression: String): Flow<List<Track>?> {
        return repository.searchTrack(expression = expression).map { result ->
            when (result) {
                is ResponseStatus.Success -> {
                    result.data
                }

                is ResponseStatus.Error -> {
                    null
                }
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