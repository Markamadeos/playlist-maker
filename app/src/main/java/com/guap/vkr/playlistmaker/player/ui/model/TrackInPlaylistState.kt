package com.guap.vkr.playlistmaker.player.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist

sealed interface TrackInPlaylistState {
    data class Exist(val playlist: Playlist) : TrackInPlaylistState
    data class Added(val playlist: Playlist) : TrackInPlaylistState
}
