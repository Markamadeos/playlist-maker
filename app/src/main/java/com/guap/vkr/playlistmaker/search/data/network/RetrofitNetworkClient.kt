package com.guap.vkr.playlistmaker.search.data.network

import com.guap.vkr.playlistmaker.search.data.NetworkClient
import com.guap.vkr.playlistmaker.search.data.dto.Response
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HttpsURLConnection

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
        }

        val response = iTunesService.search(dto.expression).execute()
        val body = response.body()

        return body?.apply { resultCode = response.code() } ?: Response().apply { resultCode = response.code() }
    }
}