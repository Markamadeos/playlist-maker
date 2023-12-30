package com.guap.vkr.playlistmaker.player.ui.model

sealed interface TrackInPlaylistState {
    object Exist : TrackInPlaylistState
    object Added : TrackInPlaylistState
}
