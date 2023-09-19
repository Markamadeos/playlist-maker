package com.guap.vkr.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.player.domain.MediaPlayerInteractor

class PlayerViewModel(
    private val playerInteractor: MediaPlayerInteractor
) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    Creator.provideMediaPlayerInteractor()
                )
            }
        }
    }
}