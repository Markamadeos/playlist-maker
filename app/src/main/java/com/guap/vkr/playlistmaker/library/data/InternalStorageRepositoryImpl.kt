package com.guap.vkr.playlistmaker.library.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.guap.vkr.playlistmaker.library.domain.api.InternalStorageRepository
import java.io.File
import java.io.FileOutputStream

class InternalStorageRepositoryImpl(private val context: Context) : InternalStorageRepository {

    override fun saveFile(uri: String, fileName: String) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PATH)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
    }

    override fun getFile(fileName: String): File {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PATH)
        return File(filePath, fileName)
    }

    companion object {
        const val PATH = "cover_images"
        const val QUALITY = 30
    }
}