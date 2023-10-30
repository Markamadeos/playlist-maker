package com.guap.vkr.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val trackUrl: String
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var clickAllowed = true

    private val stateLiveData = MutableLiveData<MediaPlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData
    init {
        renderState(MediaPlayerState.Default)
        prepareAudioPlayer()
        setOnCompleteListener()
        isClickAllowed()
    }

    private fun prepareAudioPlayer() {
        mediaPlayerInteractor.preparePlayer(trackUrl) {
            renderState(MediaPlayerState.Prepared)
        }
    }


    private fun startAudioPlayer() {
        mediaPlayerInteractor.startPlayer()
        renderState(MediaPlayerState.Playing(mediaPlayerInteractor.getCurrentPosition()))
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        renderState(MediaPlayerState.Paused)
    }


    private fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            renderState(MediaPlayerState.Prepared)
            handler.removeCallbacksAndMessages(null)
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is MediaPlayerState.Playing -> {
                pauseAudioPlayer()
            }

            is MediaPlayerState.Prepared, MediaPlayerState.Paused -> {
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
                timerLiveData.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(getCurrentPosition())
                )
                handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
            }
        }
    }

    fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 1000L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }

}