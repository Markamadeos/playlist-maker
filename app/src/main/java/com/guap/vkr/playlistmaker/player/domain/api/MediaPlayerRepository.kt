package com.guap.vkr.playlistmaker.player.domain.api

interface MediaPlayerRepository {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun getCurrentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()

    fun isPlaying(): Boolean
}