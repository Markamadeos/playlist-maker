package com.guap.vkr.playlistmaker.player.domain.api

interface MediaPlayerRepository {

    fun setDataSource(dataSource: String)

    fun prepareAsync()

    fun setOnPreparedListener(onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun start()

    fun pause()

    fun release()

    fun currentPosition(): Int
}