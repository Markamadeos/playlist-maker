package com.guap.vkr.playlistmaker.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.model.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        track = getTrack()
    }


    private fun getTrack() = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

    companion object {
        private const val TRACK = "TRACK"
    }
}