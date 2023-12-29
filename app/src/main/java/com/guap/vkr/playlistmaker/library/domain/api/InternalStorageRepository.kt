package com.guap.vkr.playlistmaker.library.domain.api

import java.io.File

interface InternalStorageRepository {
    fun saveFile(uri: String, fileName: String)
    fun getFile(fileName: String): File
}