package com.guap.vkr.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor

class TrackViewModel(
    private val trackId: Long,
    private val playerInteractor: MediaPlayerInteractor
) : ViewModel() {

//    init {
//        tracksInteractor.loadSomeData(
//            onComplete = {
//                // 2
//                loadingLiveData.postValue(false)
//                // или
//                // 3
//                loadingLiveData.value = false
//            }
//        )
//    }

    private var loadingLiveData = MutableLiveData(true)

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData

    companion object {
        fun getViewModelFactory(trackId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideMediaPlayerInteractor()

                TrackViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }
}