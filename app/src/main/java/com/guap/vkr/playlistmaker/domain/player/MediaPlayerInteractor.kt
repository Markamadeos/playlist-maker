package com.guap.vkr.playlistmaker.domain.player

import com.guap.vkr.playlistmaker.domain.player.api.MediaPlayerRepository

class MediaPlayerInteractor(private val mediaPlayerRepository: MediaPlayerRepository) {

    private var playerState = PlayerState.DEFAULT

    fun preparePlayer(
        previewUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayerRepository.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                onPreparedListener()
                playerState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                onCompletionListener()
                playerState = PlayerState.PREPARED
            }
        }
    }

    fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                onPausePlayer()
                pausePlayer(onPausePlayer)
                playerState = PlayerState.PAUSED
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                onStartPlayer()
                startPlayer(onStartPlayer)
                playerState = PlayerState.PLAYING
            }

            PlayerState.DEFAULT -> {}
        }
    }

    fun startPlayer(startPlayer: () -> Unit) {
        startPlayer()
        mediaPlayerRepository.start()
        playerState = PlayerState.PLAYING
    }

    fun pausePlayer(pausePlayer: () -> Unit) {
        pausePlayer()
        mediaPlayerRepository.pause()
        playerState = PlayerState.PAUSED
    }

    fun release() {
        mediaPlayerRepository.release()
    }

    fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    enum class PlayerState {
        DEFAULT, PREPARED, PLAYING, PAUSED
    }

}