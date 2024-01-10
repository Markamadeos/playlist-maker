package com.guap.vkr.playlistmaker.sharing.domain

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track

interface SharingInteractor {

    fun shareApp()
    fun openTerms()
    fun openSupport()

    fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
}