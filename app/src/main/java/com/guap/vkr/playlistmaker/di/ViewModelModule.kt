package com.guap.vkr.playlistmaker.di

import com.guap.vkr.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.guap.vkr.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.search.ui.view_model.SearchViewModel
import com.guap.vkr.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(
            mediaPlayerInteractor = get(),
            favoriteTracksInteractor = get(),
            track = track
        )
    }

    viewModel {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        FavoriteViewModel(favoritesInteractor = get())
    }

    viewModel {
        PlaylistViewModel()
    }
}