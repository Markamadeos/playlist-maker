package com.guap.vkr.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.guap.vkr.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private val audioPlayer = MediaPlayer()

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
        Log.e("WTF", "current pos is ${audioPlayer.currentPosition}")
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

}