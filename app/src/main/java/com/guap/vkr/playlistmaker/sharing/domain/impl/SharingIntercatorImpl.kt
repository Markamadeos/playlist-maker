package com.guap.vkr.playlistmaker.sharing.domain.impl

import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.sharing.domain.ExternalNavigator
import com.guap.vkr.playlistmaker.sharing.domain.SharingInteractor
import com.guap.vkr.playlistmaker.sharing.domain.model.EmailData

class SharingIntercatorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        externalNavigator.sharePlaylist(playlist, tracks)
    }

    private fun getShareAppLink(): String {
        return APP_URL
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(email = EMAIL)
    }

    private fun getTermsLink(): String {
        return LICENSE_URL
    }

    companion object {
        private const val EMAIL = "julipaha@gmail.com"
        private const val APP_URL = "https://practicum.yandex.ru/android-developer/"
        private const val LICENSE_URL = "https://yandex.ru/legal/practicum_offer/"
    }
}