package com.guap.vkr.playlistmaker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=")
    fun search(@Query("term") text: String): Call<SearchResponse>
}