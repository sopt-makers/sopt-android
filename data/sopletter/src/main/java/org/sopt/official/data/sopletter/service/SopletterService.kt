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
package org.sopt.official.data.sopletter.service

import org.sopt.official.data.sopletter.dto.request.UpdateSopletterMessageRequestDto
import org.sopt.official.data.sopletter.dto.response.SopletterCtaResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterTopicsResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SopletterService {
    @GET("sopt-letter/topics/default/messages")
    suspend fun getDefaultMessages(
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): SopletterMessagesResponseDto

    @GET("sopt-letter/topics/{topicId}/messages")
    suspend fun getTopicMessages(
        @Path("topicId") topicId: Long,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): SopletterMessagesResponseDto

    @GET("sopt-letter/report-form")
    suspend fun getReportForm(): SopletterReportFormResponseDto

    @GET("sopt-letter/topics/{topicId}/messages/{messageId}")
    suspend fun getMessageDetail(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
    ): SopletterMessageDetailResponseDto

    @POST("sopt-letter/topics/{topicId}/messages/{messageId}/likes")
    suspend fun addMessageLike(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
    )

    @DELETE("sopt-letter/topics/{topicId}/messages/{messageId}/likes")
    suspend fun deleteMessageLike(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
    )

    @PATCH("sopt-letter/topics/{topicId}/messages/{messageId}")
    suspend fun updateMessage(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
        @Body body: UpdateSopletterMessageRequestDto,
    ): SopletterMessageDetailResponseDto

    @DELETE("sopt-letter/topics/{topicId}/messages/{messageId}")
    suspend fun deleteMessage(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
    )

    @GET("sopt-letter/topics")
    suspend fun getTopics(
        @Query("type") type: String,
    ): SopletterTopicsResponseDto

    @GET("sopt-letter/cta")
    suspend fun getCta(): SopletterCtaResponseDto
}
