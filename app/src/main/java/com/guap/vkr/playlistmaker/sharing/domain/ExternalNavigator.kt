package com.guap.vkr.playlistmaker.sharing.domain

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {

    fun shareLink(sharedLink: String)

    fun openLink(openLink: String)

    fun openEmail(emailData: EmailData)

    fun sharePlaylist(playlist: Playlist, Tracks: List<Track>)

}