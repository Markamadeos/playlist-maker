package com.guap.vkr.playlistmaker.sharing.domain

import com.guap.vkr.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {

    fun shareLink(sharedLink: String)

    fun openLink(openLink: String)

    fun openEmail(emailData: EmailData)

}