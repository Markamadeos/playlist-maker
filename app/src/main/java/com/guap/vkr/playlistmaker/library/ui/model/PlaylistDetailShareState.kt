package com.guap.vkr.playlistmaker.library.ui.model

sealed class PlaylistDetailShareState {
    object NothingToShare : PlaylistDetailShareState()
    object Sharing : PlaylistDetailShareState()
}