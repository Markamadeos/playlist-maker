package com.guap.vkr.playlistmaker.data.network

import com.guap.vkr.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=")
    fun search(@Query("term") text: String): Call<TracksSearchResponse>
}