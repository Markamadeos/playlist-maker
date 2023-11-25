package com.guap.vkr.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.guap.vkr.playlistmaker.search.data.NetworkClient
import com.guap.vkr.playlistmaker.search.data.dto.Response
import com.guap.vkr.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.net.ssl.HttpsURLConnection

class RetrofitNetworkClient(
    private val apiService: ITunesApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.search(dto.expression)
                response.apply { resultCode = HttpsURLConnection.HTTP_OK }
            } catch (e: Throwable) {
                Response().apply { resultCode =  HttpsURLConnection.HTTP_INTERNAL_ERROR}
            }
        }

//        val response = this.apiService.search(dto.expression).execute()
//        val body = response.body()
//
//        return body?.apply { resultCode = response.code() } ?: Response().apply {
//            resultCode = response.code()
//        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}