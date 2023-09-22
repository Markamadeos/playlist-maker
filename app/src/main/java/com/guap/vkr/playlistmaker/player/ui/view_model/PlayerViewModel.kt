package com.guap.vkr.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState


class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val trackUrl: String
) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<MediaPlayerState>()

    // private val playTimeLiveData = MutableLiveData<Int>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    //fun observeTime(): LiveData<Int> = playTimeLiveData

    init {
        renderState(MediaPlayerState.DEFAULT)
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        mediaPlayerInteractor.preparePlayer(trackUrl) {
            renderState(MediaPlayerState.PREPARED)
        }
    }


    private fun startAudioPlayer() {
        mediaPlayerInteractor.startPlayer()
        renderState(MediaPlayerState.PLAYING(mediaPlayerInteractor.getCurrentPosition()))
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        renderState(MediaPlayerState.PAUSED)
    }


    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            Log.e("WTF", "COMLETED in viewModel")
            renderState(MediaPlayerState.PREPARED)
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is MediaPlayerState.PLAYING -> {
                pauseAudioPlayer()
            }

            is MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                startAudioPlayer()
            }

            else -> {}
        }
    }

    private fun renderState(state: MediaPlayerState) {
        stateLiveData.postValue(state)
    }


    override fun onCleared() {
        mediaPlayerInteractor.destroyPlayer()
    }


    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory() {
            initializer {
                PlayerViewModel(Creator.provideMediaPlayerInteractor(), url)
            }
        }
    }

}