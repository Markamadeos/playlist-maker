package com.guap.vkr.playlistmaker.screens

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.model.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artist: TextView
    private lateinit var addButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var playtime: TextView
    private lateinit var duration: TextView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track = getTrack()
        initVariables()
        bind(track)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initVariables() {
        backButton = findViewById(R.id.btn_back)
        cover = findViewById(R.id.iv_cover)
        trackName = findViewById(R.id.tv_track_name)
        artist = findViewById(R.id.tv_artist_name)
        addButton = findViewById(R.id.btn_add)
        playButton = findViewById(R.id.btn_play)
        likeButton = findViewById(R.id.btn_like)
        playtime = findViewById(R.id.tv_playtime)
        duration = findViewById(R.id.tv_duration_value)
        album = findViewById(R.id.tv_album_value)
        year = findViewById(R.id.tv_year_value)
        genre = findViewById(R.id.tv_genre_value)
        country = findViewById(R.id.tv_country_value)
    }
        private fun bind(track: Track) {
            val cornerRadius = this.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.iv_track_cover)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(cover)

            trackName.text = track.trackName
            artist.text = track.artistName
            playtime.text = track.getDuration()
            duration.text = track.getDuration()
            album.text = track.collectionName
            year.text = track.getReleaseYear()
            genre.text = track.primaryGenreName
            country.text = track.country
        }

        private fun getTrack() = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        companion object {
            private const val TRACK = "TRACK"
        }
    }