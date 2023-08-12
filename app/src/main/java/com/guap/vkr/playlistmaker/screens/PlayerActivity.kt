package com.guap.vkr.playlistmaker.screens

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivityPlayerBinding
import com.guap.vkr.playlistmaker.model.Track
import com.guap.vkr.playlistmaker.utils.TRACK
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = getTrack()
        bind(track, binding)
        preparePlayer(track)

        binding.btnPlay.setOnClickListener {
            if (clickDebounce()) {
                playbackControl()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bind(track: Track, binding: ActivityPlayerBinding) {
        val cornerRadius = this.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.iv_track_cover)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivCover)

        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvPlaytime.text = getString(R.string.default_playtime_value)
        binding.tvDurationValue.text = track.getDuration()
        binding.tvAlbumValue.text = track.collectionName
        binding.tvYearValue.text = track.getReleaseYear()
        binding.tvGenreValue.text = track.primaryGenreName
        binding.tvCountryValue.text = track.country
    }

    private fun getTrack() = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

    private fun preparePlayer(track: Track) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
                handler.removeCallbacksAndMessages(null)
                binding.tvPlaytime.text = getString(R.string.default_playtime_value)
                updatePlayButton()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        handler.post(getCurrentPlaybackPosition())
        updatePlayButton()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        binding.btnPlay.setImageResource(R.drawable.ic_play)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
            STATE_PLAYING -> pausePlayer()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    private fun getCurrentPlaybackPosition(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.tvPlaytime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
                }
            }

        }
    }

    private fun updatePlayButton() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT ->
                binding.btnPlay.setImageResource(R.drawable.ic_play)

            STATE_PLAYING -> binding.btnPlay.setImageResource(R.drawable.ic_pause)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 1000L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}