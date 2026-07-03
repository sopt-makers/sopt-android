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
package org.sopt.official.data.sopletter.repository

import javax.inject.Inject
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.mapper.toDomain
import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
import org.sopt.official.domain.sopletter.model.SopletterMessageDetail
import org.sopt.official.domain.sopletter.model.SopletterTopic
import org.sopt.official.domain.sopletter.repository.SopletterRepository

internal class SopletterRepositoryImpl @Inject constructor(
    private val sopletterDataSource: SopletterDataSource,
) : SopletterRepository {
    override suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): Result<SopletterDefaultMessages> = suspendRunCatching {
        sopletterDataSource.getDefaultMessages(
            cursor = cursor,
            size = size,
        ).toDomain()
    }

    override suspend fun getReportFormUrl(): Result<String> = suspendRunCatching {
        sopletterDataSource.getReportForm().reportFormUrl
    }

    override suspend fun getMessageDetail(
        topicId: Long,
        messageId: Long,
    ): Result<SopletterMessageDetail> = suspendRunCatching {
        sopletterDataSource.getMessageDetail(
            topicId = topicId,
            messageId = messageId,
        ).toDomain()
    }

    override suspend fun addMessageLike(
        topicId: Long,
        messageId: Long,
    ): Result<Unit> = suspendRunCatching {
        sopletterDataSource.addMessageLike(
            topicId = topicId,
            messageId = messageId,
        )
    }

    override suspend fun deleteMessageLike(
        topicId: Long,
        messageId: Long,
    ): Result<Unit> = suspendRunCatching {
        sopletterDataSource.deleteMessageLike(
            topicId = topicId,
            messageId = messageId,
        )
    }

    override suspend fun updateMessage(
        topicId: Long,
        messageId: Long,
        content: String,
    ): Result<SopletterMessageDetail> = suspendRunCatching {
        sopletterDataSource.updateMessage(
            topicId = topicId,
            messageId = messageId,
            content = content,
        ).toDomain()
    }

    override suspend fun deleteMessage(
        topicId: Long,
        messageId: Long,
    ): Result<Unit> = suspendRunCatching {
        sopletterDataSource.deleteMessage(
            topicId = topicId,
            messageId = messageId,
        )
    }

    override suspend fun getTopics(): Result<List<SopletterTopic>> = suspendRunCatching {
        sopletterDataSource.getTopics(type = SopletterRepository.TOPIC_TYPE_NORMAL).toDomain()
    }
}
