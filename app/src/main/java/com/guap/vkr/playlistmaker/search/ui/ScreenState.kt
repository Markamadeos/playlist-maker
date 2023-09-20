package com.guap.vkr.playlistmaker.search.ui

import com.guap.vkr.playlistmaker.search.domain.model.Track

sealed interface ScreenState {

    object Loading : ScreenState

    data class Content(val tracks: List<Track>) : ScreenState

    class Error() : ScreenState

    class Empty() : ScreenState

    data class ContentHistoryList(val historyList: List<Track>) : ScreenState

    class EmptyHistoryList() : ScreenState

}