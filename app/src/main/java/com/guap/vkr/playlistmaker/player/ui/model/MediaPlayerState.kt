package com.guap.vkr.playlistmaker.player.ui.model


sealed interface MediaPlayerState {
    object Default : MediaPlayerState
    object Prepared : MediaPlayerState
    data class Playing(
        val time: Int
    ) : MediaPlayerState

    object Paused : MediaPlayerState
}