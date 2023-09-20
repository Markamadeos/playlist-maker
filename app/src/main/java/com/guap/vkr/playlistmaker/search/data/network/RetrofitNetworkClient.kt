package com.guap.vkr.playlistmaker.search.data.network

import com.guap.vkr.playlistmaker.search.data.NetworkClient
import com.guap.vkr.playlistmaker.search.data.dto.Response
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchRequest
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
//        if (!isConnected()) {
//            return Response().apply { resultCode = -1 }
//        }

        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        val response = iTunesService.search(dto.expression).execute()
        val body = response.body()

        return body?.apply { resultCode = response.code() } ?: Response().apply { resultCode = response.code() }
    }

//    private fun isConnected(): Boolean {
//        val connectivityManager = context.getSystemService(
//            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//        if (capabilities != null) {
//            when {
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
//            }
//        }
//        return false
//    }

    companion object{
        private const val BAD_REQUEST = 400
    }
}