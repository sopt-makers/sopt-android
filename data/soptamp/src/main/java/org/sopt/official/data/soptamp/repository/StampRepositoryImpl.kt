/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.sopt.official.data.soptamp.ContentUriRequestBody
import org.sopt.official.data.soptamp.remote.api.StampService
import org.sopt.official.domain.soptamp.model.Archive
import org.sopt.official.domain.soptamp.model.ImageModel
import org.sopt.official.domain.soptamp.repository.StampRepository
import javax.inject.Inject

class StampRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: StampService,
    private val json: Json
) : StampRepository {
    @Serializable
    private data class Content(
        @SerialName("contents")
        val contents: String
    )

    override suspend fun completeMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ): Result<Unit> {
        val contentJson = json.encodeToString(Content(content))
        val contentRequestBody = contentJson.toRequestBody("application/json".toMediaType())
        val imageRequestBody = when (imageUri) {
            is ImageModel.Empty -> null
            is ImageModel.Local -> {
                imageUri.uri.map {
                    ContentUriRequestBody(context, Uri.parse(it))
                }.map {
                    it.toFormData("imgUrl")
                }
            }

            is ImageModel.Remote -> null
        }
        return runCatching {
            service.registerStamp(
                missionId = missionId,
                stampContent = contentRequestBody,
                imgUrl = imageRequestBody
            )
        }
    }

    override suspend fun getMissionContent(
        missionId: Int,
        nickname: String
    ): Result<Archive> {
        return runCatching {
            service.retrieveStamp(
                missionId = missionId,
                nickname = nickname
            ).toDomain()
        }
    }

    override suspend fun modifyMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ): Result<Unit> {
        val contentJson = json.encodeToString(Content(content))
        val contentRequestBody = contentJson.toRequestBody("application/json".toMediaType())
        val imageRequestBody = when (imageUri) {
            is ImageModel.Empty -> null
            is ImageModel.Local -> {
                imageUri.uri.map {
                    ContentUriRequestBody(context, Uri.parse(it))
                }.map {
                    it.toFormData("imgUrl")
                }
            }

            is ImageModel.Remote -> listOf(ContentUriRequestBody(context, null).toFormData("imgUrl"))
        }
        return runCatching {
            service.modifyStamp(
                missionId = missionId,
                stampContent = contentRequestBody,
                imgUrl = imageRequestBody
            )
        }
    }

    override suspend fun deleteMission(missionId: Int): Result<Unit> {
        return runCatching {
            service.deleteStamp(
                missionId = missionId
            )
        }
    }

    override suspend fun deleteAllStamps(): Result<Unit> {
        return runCatching { service.deleteAllStamps() }
    }
}
