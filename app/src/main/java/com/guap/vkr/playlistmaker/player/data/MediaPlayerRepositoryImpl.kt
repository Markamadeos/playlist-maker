package com.guap.vkr.playlistmaker.player.data

import android.media.MediaPlayer
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val audioPlayer: MediaPlayer) : MediaPlayerRepository {

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        audioPlayer.setDataSource(url)
        audioPlayer.prepareAsync()
        audioPlayer.setOnPreparedListener {
            onPreparedListener()
        }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        audioPlayer.setOnCompletionListener { onCompletionListener() }
    }

    override fun getCurrentPosition(): Int {
        return audioPlayer.currentPosition
    }

    override fun startPlayer() {
        audioPlayer.start()
    }

    override fun pausePlayer() {
        audioPlayer.pause()
    }

    override fun destroyPlayer() {
        audioPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return audioPlayer.isPlaying
    }

}