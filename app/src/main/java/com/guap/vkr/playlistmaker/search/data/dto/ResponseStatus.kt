package com.guap.vkr.playlistmaker.search.data.dto

sealed class ResponseStatus<T>(val data: T? = null, val hasError: Boolean? = false) {
    class Success<T>(data: T): ResponseStatus<T>(data)
    class Error<T>(data: T? = null): ResponseStatus<T>(data, true)
}