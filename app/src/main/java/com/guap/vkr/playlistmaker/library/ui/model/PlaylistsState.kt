package com.guap.vkr.playlistmaker.library.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistsState {
    object StateEmpty : PlaylistsState
    data class StateContent(val playlists: List<Playlist>) : PlaylistsState
}