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