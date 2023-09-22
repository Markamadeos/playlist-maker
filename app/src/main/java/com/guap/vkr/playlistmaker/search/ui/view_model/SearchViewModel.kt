package com.guap.vkr.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.search.domain.SearchInteractor
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.model.ScreenState

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val _stateLiveData = MutableLiveData<ScreenState>()
    fun stateLiveData(): LiveData<ScreenState> = _stateLiveData

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String, hasError: Boolean) {
        if (latestSearchText == changedText && !hasError) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MS
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun search(expression: String) {
        if (expression.isNotEmpty()) {
            renderState(ScreenState.Loading)

            searchInteractor.searchTracks(expression, object : SearchInteractor.SearchConsumer {
                override fun consume(foundTracks: List<Track>?, hasError: Boolean?) {
                    val tracks = mutableListOf<Track>()

                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)

                        when {
                            tracks.isEmpty() -> {
                                renderState(ScreenState.Empty())
                            }

                            tracks.isNotEmpty() -> {
                                renderState(ScreenState.Content(tracks))
                            }
                        }
                    } else {
                        renderState(ScreenState.Error())
                    }
                }
            })
        }
    }

    fun getTracksHistory() {
        searchInteractor.getTracksHistory(object : SearchInteractor.HistoryConsumer {
            override fun consume(tracks: List<Track>?) {
                if (tracks.isNullOrEmpty()) {
                    renderState(ScreenState.EmptyHistoryList())
                } else {
                    renderState(ScreenState.ContentHistoryList(tracks))
                }
            }
        })
    }

    fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    private fun renderState(state: ScreenState) {
        _stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchInteractor = Creator.provideSearchInteractor(context)
                )
            }
        }

        private const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}