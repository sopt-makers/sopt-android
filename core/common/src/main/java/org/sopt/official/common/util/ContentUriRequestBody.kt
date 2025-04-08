/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import java.io.ByteArrayOutputStream
import kotlin.math.min
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class ContentUriRequestBody(
    context: Context,
    private val uri: Uri?
) : RequestBody() {
    private val contentResolver = context.contentResolver
    private var compressedImage: ByteArray? = null
    private var metadata: ImageMetadata? = null

    private data class ImageMetadata(
        val fileName: String,
        val size: Long,
        val mimeType: String?
    ) {
        companion object {
            val EMPTY = ImageMetadata("", 0L, null)
        }
    }

    init {
        uri?.let {
            metadata = extractMetadata(it)
            compressImage()
        }
    }

    private fun extractMetadata(uri: Uri): ImageMetadata =
        runCatching {
            contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.MIME_TYPE
                ),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    ImageMetadata(
                        fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                        size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                        mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                    )
                } else {
                    ImageMetadata.EMPTY
                }
            } ?: ImageMetadata.EMPTY
        }.getOrDefault(ImageMetadata.EMPTY)

    /**
     * 이미지를 압축하는 함수입니다.
     * 이미지 크기가 MAX_FILE_SIZE(1MB)보다 작으면 압축을 건너뛰고 100% 품질로 처리합니다.
     * 크기가 클 경우 calculateQuality() 함수를 통해 압축률을 적용하고,
     * calculateTargetSize() 함수를 통해 MAX_WIDTH(1024px)와 MAX_HEIGHT(1024px)를
     * 초과하지 않도록 이미지 크기를 조정합니다.
     */
    private fun compressImage() {
        if (uri == null) return

        try {
            val source = ImageDecoder.createSource(contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
                val size = calculateTargetSize(info.size.width, info.size.height)
                decoder.setTargetSize(size.width, size.height)
            }

            ByteArrayOutputStream().use { buffer ->
                val fileSize = metadata?.size ?: 0L

                if (fileSize <= MAX_FILE_SIZE) {
                    // 파일 크기가 작으면 압축을 건너 뜁니다.
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer)
                } else {
                    val quality = calculateQuality(fileSize)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, buffer)
                }

                compressedImage = buffer.toByteArray()
                bitmap.recycle()
            }
        } catch (e: Exception) {
            compressedImage = ByteArray(0)
        }
    }

    private fun calculateQuality(fileSize: Long): Int {
        // 파일 크기에 따라 품질 조정 (크기가 클수록 낮은 품질을 선택합니다)
        return when {
            fileSize > 5 * 1024 * 1024 -> 60  // 5MB 초과
            fileSize > 3 * 1024 * 1024 -> 70  // 3MB 초과
            fileSize > 1 * 1024 * 1024 -> 80  // 1MB 초과
            else -> 90                        // 1MB 이하
        }
    }

    private fun calculateTargetSize(width: Int, height: Int): Size {
        if (width <= MAX_WIDTH && height <= MAX_HEIGHT) {
            return Size(width, height)
        }
        val scaleFactor = min(MAX_WIDTH / width.toFloat(), MAX_HEIGHT / height.toFloat())
        val targetWidth = (width * scaleFactor).toInt()
        val targetHeight = (height * scaleFactor).toInt()
        return Size(targetWidth, targetHeight)
    }

    override fun contentLength(): Long = compressedImage?.size?.toLong() ?: -1L

    override fun contentType(): MediaType? = metadata?.mimeType?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }

    companion object {
        const val MAX_WIDTH = 1024
        const val MAX_HEIGHT = 1024
        const val MAX_FILE_SIZE = 1024 * 1024
    }
}
