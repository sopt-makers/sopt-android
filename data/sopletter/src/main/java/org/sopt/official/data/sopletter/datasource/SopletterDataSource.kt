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
package org.sopt.official.data.sopletter.datasource

import org.sopt.official.data.sopletter.dto.response.SopletterCtaResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterTopicsResponseDto

interface SopletterDataSource {
    suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): SopletterMessagesResponseDto

    suspend fun getTopicMessages(
        topicId: Long,
        cursor: Long?,
        size: Int,
    ): SopletterMessagesResponseDto

    suspend fun getReportForm(): SopletterReportFormResponseDto

    suspend fun getMessageDetail(
        topicId: Long,
        messageId: Long,
    ): SopletterMessageDetailResponseDto

    suspend fun addMessageLike(
        topicId: Long,
        messageId: Long,
    )

    suspend fun deleteMessageLike(
        topicId: Long,
        messageId: Long,
    )

    suspend fun updateMessage(
        topicId: Long,
        messageId: Long,
        content: String,
    ): SopletterMessageDetailResponseDto

    suspend fun deleteMessage(
        topicId: Long,
        messageId: Long,
    )

    suspend fun getTopics(
        type: String,
    ): SopletterTopicsResponseDto

    suspend fun getCta(): SopletterCtaResponseDto
}
