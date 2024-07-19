/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.sopt.official.common.util.ContentUriRequestBody
import org.sopt.official.data.soptamp.remote.api.S3Service
import org.sopt.official.data.soptamp.remote.api.StampService
import org.sopt.official.domain.soptamp.model.ImageUploadUrl
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository

class ImageUploaderRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val s3Service: S3Service,
    private val stampService: StampService
) : ImageUploaderRepository {

    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {
        s3Service.putS3Image(
            preSignedURL,
            imageUri.run {
                val uri = Uri.parse(this)
                val requestBody = ContentUriRequestBody(context, uri)
                requestBody
            }
        )
    }

    override suspend fun getImageUploadURL(): Result<ImageUploadUrl> {
        return runCatching { stampService.getS3URL().toDomain() }
    }
}
