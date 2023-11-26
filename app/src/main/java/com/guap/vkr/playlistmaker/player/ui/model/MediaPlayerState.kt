package com.guap.vkr.playlistmaker.player.ui.model


sealed interface MediaPlayerState {
    object Default : MediaPlayerState
    object Prepared : MediaPlayerState
    object Playing : MediaPlayerState

    object Paused : MediaPlayerState
}