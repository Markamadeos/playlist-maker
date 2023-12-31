package com.guap.vkr.playlistmaker.library.domain.api

interface InternalStorageRepository {
    fun saveFile(uri: String, fileName: String)
    fun getFile(fileName: String): String
}