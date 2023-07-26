package com.guap.vkr.playlistmaker.screens

import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivityPlayerBinding
import com.guap.vkr.playlistmaker.model.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = getTrack()
        bind(track, binding)

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
        binding.tvPlaytime.text = track.getDuration()
        binding.tvDurationValue.text = track.getDuration()
        binding.tvAlbumValue.text = track.collectionName
        binding.tvYearValue.text = track.getReleaseYear()
        binding.tvGenreValue.text = track.primaryGenreName
        binding.tvCountryValue.text = track.country
    }

    private fun getTrack() = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

}