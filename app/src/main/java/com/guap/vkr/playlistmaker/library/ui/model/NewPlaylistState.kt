package com.guap.vkr.playlistmaker.library.ui.model

sealed interface NewPlaylistState {
    object Empty : NewPlaylistState
    object Created : NewPlaylistState
}