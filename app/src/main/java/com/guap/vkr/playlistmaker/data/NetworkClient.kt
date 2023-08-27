package com.guap.vkr.playlistmaker.data

import com.guap.vkr.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}