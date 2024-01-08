package com.guap.vkr.playlistmaker.library.ui.model

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track

sealed class PlaylistDetailState {
    data class Empty(val playlist: Playlist) : PlaylistDetailState()
    data class Content(val tracks: List<Track>, val playlist: Playlist, val duration: Long) :
        PlaylistDetailState()
}