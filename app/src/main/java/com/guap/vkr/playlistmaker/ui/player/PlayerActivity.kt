package com.guap.vkr.playlistmaker.ui.player

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
import com.guap.vkr.playlistmaker.domain.models.Track
import com.guap.vkr.playlistmaker.utils.TRACK
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var binding: ActivityPlayerBinding? = null
    private val mediaPlayer = MediaPlayer()
    private var playerState = State.DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var clickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val track = getTrack()
        bind(track)
        preparePlayer(track)

        binding?.btnPlay?.setOnClickListener {
            if (isClickAllowed()) {
                playbackControl()
            }
        }

        binding?.btnBack?.setOnClickListener {
            finish()
        }
    }

    private fun bind(track: Track) {
        val cornerRadius = this.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.iv_track_cover)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding!!.ivCover)

        binding?.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvPlaytime.text = getString(R.string.default_playtime_value)
            tvDurationValue.text = track.getDuration()
            tvAlbumValue.text = track.collectionName
            tvYearValue.text = track.getReleaseYear()
            tvGenreValue.text = track.primaryGenreName
            tvCountryValue.text = track.country
        }
    }

    private fun getTrack() = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

    private fun preparePlayer(track: Track) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = State.PREPARED
            }
            setOnCompletionListener {
                playerState = State.PREPARED
                handler.removeCallbacksAndMessages(getCurrentPlaybackPosition())
                binding?.tvPlaytime?.text = getString(R.string.default_playtime_value)
                updatePlayButton()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
        handler.post(getCurrentPlaybackPosition())
        updatePlayButton()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
        updatePlayButton()
    }

    private fun playbackControl() {
        when (playerState) {
            State.PAUSED, State.PREPARED -> startPlayer()
            State.PLAYING -> pausePlayer()
            else -> {}
        }
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    private fun getCurrentPlaybackPosition(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == State.PLAYING) {
                    binding?.tvPlaytime?.text = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
                }
            }
        }
    }

    private fun updatePlayButton() {
        binding?.btnPlay?.setImageResource(
            if (playerState == State.PLAYING)
                R.drawable.ic_pause else
                R.drawable.ic_play
        )
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }

    enum class State {
        DEFAULT, PREPARED, PLAYING, PAUSED
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }
}