package com.guap.vkr.playlistmaker.data.network

import com.guap.vkr.playlistmaker.data.NetworkClient
import com.guap.vkr.playlistmaker.data.dto.Response
import com.guap.vkr.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = BAD_REQUEST }
        }
    }

    companion object {
        private const val BAD_REQUEST = 400
    }
}