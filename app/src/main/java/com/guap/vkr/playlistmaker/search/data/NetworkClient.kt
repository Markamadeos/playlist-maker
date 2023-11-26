package com.guap.vkr.playlistmaker.search.data

import com.guap.vkr.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}