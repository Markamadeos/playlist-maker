package com.guap.vkr.playlistmaker.library.ui.model

import com.guap.vkr.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {

    data class Content(val tracks: List<Track>) : FavoriteState
    object Empty : FavoriteState
}
