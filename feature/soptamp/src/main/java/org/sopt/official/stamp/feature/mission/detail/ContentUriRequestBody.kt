package org.sopt.official.stamp.feature.mission.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.ByteArrayOutputStream

class ContentUriRequestBody(
    context: Context,
    private val uri: Uri?,
) : RequestBody() {
    private val contentResolver = context.contentResolver

    private var fileName = ""
    private var size = -1L
    private var compressedImage: ByteArray? = null

    init {
        if (uri != null) {
            contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
                null,
                null,
                null,
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    fileName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                }
            }

            // Compress bitmap
            compressBitmap()
        }
    }

    private fun compressBitmap() {
        if (uri != null) {
            val originalBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            val outputStream = ByteArrayOutputStream()
            val imageSizeMb = size / (1024 * 1024.toDouble())
            outputStream.use {
                val compressRate = ((3 / imageSizeMb) * 100).toInt()
                originalBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    if (imageSizeMb >= 3) compressRate else 100,
                    it,
                )
            }
            compressedImage = outputStream.toByteArray()
            size = compressedImage?.size?.toLong() ?: -1L
        }
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        uri?.let { contentResolver.getType(it)?.toMediaTypeOrNull() }

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }

    fun toFormData(name: String) = MultipartBody.Part.createFormData(name, getFileName(), this)
}
