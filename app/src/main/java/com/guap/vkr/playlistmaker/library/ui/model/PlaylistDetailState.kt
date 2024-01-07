package com.guap.vkr.playlistmaker.library.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist

sealed class PlaylistDetailState {
    object Empty : PlaylistDetailState()
    data class Content(val playlist: Playlist) :
        PlaylistDetailState()
}