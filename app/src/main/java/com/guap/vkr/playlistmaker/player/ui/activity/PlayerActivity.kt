package com.guap.vkr.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivityPlayerBinding
import com.guap.vkr.playlistmaker.player.domain.model.Track
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import com.guap.vkr.playlistmaker.utils.TRACK

class PlayerActivity : AppCompatActivity() {

    private var binding: ActivityPlayerBinding? = null

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val track = getTrack()
        bind(track)

        viewModel = ViewModelProvider(
            this, PlayerViewModel
                .getViewModelFactory(track.previewUrl)
        )[PlayerViewModel::class.java]

        viewModel.observeState().observe(this) {
            updateScreen(it)
        }

        viewModel.observeTimer().observe(this) {
            updateTimer(it)
        }

        binding?.btnPlay?.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
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
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
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

    private fun updateTimer(time: String) {
        binding?.tvPlaytime?.text = time
    }

    private fun updateScreen(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.PLAYING -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_pause)
            }

            is MediaPlayerState.PAUSED -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
            }

            is MediaPlayerState.PREPARED -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
                binding?.tvPlaytime?.setText(R.string.default_playtime_value)
            }

            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}