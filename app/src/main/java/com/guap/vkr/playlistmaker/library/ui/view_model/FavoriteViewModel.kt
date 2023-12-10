package com.guap.vkr.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guap.vkr.playlistmaker.library.domain.api.LibraryInteractor
import com.guap.vkr.playlistmaker.library.ui.model.FavoriteState
import com.guap.vkr.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val libraryInteractor: LibraryInteractor
) : ViewModel() {

    private val stateLivedata = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLivedata

    fun updateFavoriteTracksList() {
        viewModelScope.launch {
            libraryInteractor.getFavoriteTracks().collect {
                setScreenState(it)
            }
        }
    }

    private fun setScreenState(tracks: List<Track>) {
        stateLivedata.postValue(
            if (tracks.isEmpty()) FavoriteState.Empty else FavoriteState.Content(
                tracks = tracks
            )
        )
    }

}