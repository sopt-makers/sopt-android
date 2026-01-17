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
package org.sopt.official.data.soptamp.repository

import android.app.Application
import androidx.core.net.toUri
import dev.zacsweers.metro.Inject
import org.sopt.official.common.util.ContentUriRequestBody
import org.sopt.official.data.soptamp.remote.api.S3Service
import org.sopt.official.data.soptamp.remote.api.StampService
import org.sopt.official.domain.soptamp.model.ImageUploadUrl
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository

class ImageUploaderRepositoryImpl @Inject constructor(
    private val application: Application,
    private val s3Service: S3Service,
    private val stampService: StampService
) : ImageUploaderRepository {

    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {
        // 이미지 URI를 ContentUriRequestBody로 변환하여 S3에 업로드
        val requestBody = ContentUriRequestBody(application.applicationContext, imageUri.toUri())
        s3Service.putS3Image(preSignedURL, requestBody)
    }

    override suspend fun getImageUploadURL(): Result<ImageUploadUrl> {
        return runCatching { stampService.getS3URL().toDomain() }
    }
}