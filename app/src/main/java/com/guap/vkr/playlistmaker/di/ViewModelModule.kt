package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (trackUrl: String) ->
        PlayerViewModel(mediaPlayerInteractor = get(), trackUrl = trackUrl)
    }
}