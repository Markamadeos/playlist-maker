package com.guap.vkr.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivityPlayerBinding
import com.guap.vkr.playlistmaker.player.ui.model.MediaPlayerState
import com.guap.vkr.playlistmaker.player.ui.view_model.PlayerViewModel
import com.guap.vkr.playlistmaker.search.domain.model.Track
import com.guap.vkr.playlistmaker.utils.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(getTrack().previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bind(getTrack())

        viewModel.observeState().observe(this) {
            updateScreen(it)
        }

        viewModel.observeTimer().observe(this) {
            updateTimer(it)
        }

        binding.btnPlay.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bind(track: Track) {
        val cornerRadius = this.resources.getDimensionPixelSize(R.dimen._8dp)

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.iv_track_cover)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(binding.ivCover)

        binding.apply {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvPlaytime.text = getString(R.string.default_playtime_value)
            tvDurationValue.text = track.getDuration()
            tvAlbumValue.text = track.collectionName
            tvYearValue.text = track.getReleaseYear()
            tvGenreValue.text = track.primaryGenreName
            tvCountryValue.text = track.country

            tvTrackName.isSelected = true
            tvArtistName.isSelected = true
        }
    }

    private fun getTrack() =
        Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

    private fun updateTimer(time: String) {
        binding.tvPlaytime.text = time
    }

    private fun updateScreen(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.Playing -> {
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
            }

            is MediaPlayerState.Paused -> {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
            }

            is MediaPlayerState.Prepared -> {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
                binding.tvPlaytime.setText(R.string.default_playtime_value)
            }

            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}