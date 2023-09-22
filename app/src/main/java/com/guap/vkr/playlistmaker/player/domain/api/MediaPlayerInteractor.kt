package com.guap.vkr.playlistmaker.player.domain.api

interface MediaPlayerInteractor {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun getCurrentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()
}