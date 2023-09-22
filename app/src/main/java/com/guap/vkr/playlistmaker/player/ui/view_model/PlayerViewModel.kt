package com.guap.vkr.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor, private val trackUrl: String) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MediaPlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    private val clickAllowLiveData = MutableLiveData<Boolean>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData
    fun observeClickAllow(): LiveData<Boolean> = clickAllowLiveData

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


    private fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
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
                handler.post(updateTime())
            }

            else -> {}
        }
    }

    private fun renderState(state: MediaPlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayerInteractor.destroyPlayer()
    }

    fun onPause() {
        pauseAudioPlayer()
        handler.removeCallbacksAndMessages(updateTime())
    }

    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                timerLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(getCurrentPosition()))
                handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
            }
        }
    }

    companion object {

        private const val PLAYBACK_UPDATE_DELAY_MS = 300L

        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory() {
            initializer {
                PlayerViewModel(Creator.provideMediaPlayerInteractor(), url)
            }
        }
    }

}