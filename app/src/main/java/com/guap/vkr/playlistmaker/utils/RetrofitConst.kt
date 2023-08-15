package com.guap.vkr.playlistmaker.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val iTunesBaseUrl = "https://itunes.apple.com/"

val retrofit: Retrofit =
    Retrofit.Builder().baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()