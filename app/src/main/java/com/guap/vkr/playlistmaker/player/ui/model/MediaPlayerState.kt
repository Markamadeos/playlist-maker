package com.guap.vkr.playlistmaker.player.ui.model


sealed interface MediaPlayerState {
    object DEFAULT : MediaPlayerState
    object PREPARED : MediaPlayerState
    data class PLAYING(
        val time: Int
    ) : MediaPlayerState

    object PAUSED : MediaPlayerState
}