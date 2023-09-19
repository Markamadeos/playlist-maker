package com.guap.vkr.playlistmaker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.creator.Creator
import com.guap.vkr.playlistmaker.databinding.ActivityPlayerBinding
import com.guap.vkr.playlistmaker.player.domain.model.Track
import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import com.guap.vkr.playlistmaker.utils.TRACK
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : ComponentActivity() {

    private var binding: ActivityPlayerBinding? = null
    private val mediaPlayerIInteractor = Creator.provideMediaPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private var clickAllowed = true
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(
            this, PlayerViewModel
                .getViewModelFactory()
        )[PlayerViewModel::class.java]

        val track = getTrack()
        bind(track)
        preparePlayer(track)

        binding?.btnPlay?.setOnClickListener {
            if (isClickAllowed()) {
                mediaPlayerIInteractor.playbackControl(
                    {
                        binding?.btnPlay?.setImageResource(R.drawable.ic_pause)
                        handler.post(getCurrentPlaybackPosition())
                    },
                    {
                        handler.removeCallbacksAndMessages(getCurrentPlaybackPosition())
                        binding?.btnPlay?.setImageResource(R.drawable.ic_play)
                    })
            }
        }

        binding?.btnBack?.setOnClickListener {
            finish()
        }
    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        //empty
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
        mediaPlayerIInteractor.preparePlayer(
            track.previewUrl,
            {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
            },
            {
                handler.removeCallbacksAndMessages(getCurrentPlaybackPosition())
                binding?.tvPlaytime?.text = getString(R.string.default_playtime_value)
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
            }
        )
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
                binding?.tvPlaytime?.text = SimpleDateFormat(
                    "mm:ss", Locale.getDefault()
                ).format(mediaPlayerIInteractor.currentPosition())
                handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerIInteractor.pausePlayer { binding?.btnPlay?.setImageResource(R.drawable.ic_play) }
        handler.removeCallbacksAndMessages(getCurrentPlaybackPosition())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayerIInteractor.release()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 500L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }
}