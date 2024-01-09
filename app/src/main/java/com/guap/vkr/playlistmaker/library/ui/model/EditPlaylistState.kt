package com.guap.vkr.playlistmaker.library.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist

sealed class EditPlaylistState {
    data class FilledPlaylistData(val playlist: Playlist) : EditPlaylistState()
}