/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.print.manager

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfDocument
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sopt.official.common.coroutines.suspendRunCatching

object PdfHelper {

    class ProgressiveWriter(private val fileName: String) {
        private val pdfDocument = PdfDocument()
        private val pdfWidth = 595f
        private var pageIndex = 0

        fun addPageAndRecycle(bitmap: Bitmap) {
            val softwareBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
                bitmap.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                bitmap
            }

            val scale = pdfWidth / softwareBitmap.width.toFloat()
            val pdfHeight = (softwareBitmap.height * scale).toInt()

            val pageInfo = PdfDocument.PageInfo.Builder(pdfWidth.toInt(), pdfHeight, ++pageIndex).create()
            val matrix = Matrix().apply { postScale(scale, scale) }

            try {
                val page = pdfDocument.startPage(pageInfo)
                page.canvas.drawBitmap(softwareBitmap, matrix, null)
                pdfDocument.finishPage(page)
            } finally {
                if (softwareBitmap !== bitmap) {
                    softwareBitmap.recycle()
                }
                bitmap.recycle()
            }
        }

        suspend fun saveAndClose(context: Context): Result<Unit> = withContext(Dispatchers.IO) {
            suspendRunCatching {
                try {
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                    }

                    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                        ?: error("MediaStore insert 실패")

                    try {
                        resolver.openOutputStream(uri)?.use { stream ->
                            pdfDocument.writeTo(stream)
                        } ?: error("OutputStream open 실패")
                    } catch (e: Exception) {
                        resolver.delete(uri, null, null)
                        throw e
                    }
                } finally {
                    pdfDocument.close()
                }
            }
        }

        fun close() {
            pdfDocument.close()
        }
    }
}
