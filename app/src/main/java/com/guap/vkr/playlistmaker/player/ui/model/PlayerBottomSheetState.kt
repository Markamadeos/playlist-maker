package com.guap.vkr.playlistmaker.player.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist

sealed interface PlayerBottomSheetState {
    object Empty : PlayerBottomSheetState
    data class Content(val playlists: List<Playlist>) : PlayerBottomSheetState
}