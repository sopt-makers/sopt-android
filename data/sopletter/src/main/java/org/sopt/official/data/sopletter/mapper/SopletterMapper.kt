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
package org.sopt.official.data.sopletter.mapper
import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDto
import org.sopt.official.data.sopletter.dto.response.SopletterTopicDto
import org.sopt.official.data.sopletter.dto.response.SopletterTopicsResponseDto
import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
import org.sopt.official.domain.sopletter.model.SopletterMessageDetail
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.domain.sopletter.model.SopletterShapeType
import org.sopt.official.domain.sopletter.model.SopletterTopic

internal fun SopletterTopicsResponseDto.toDomain(): List<SopletterTopic> = topics.map(SopletterTopicDto::toDomain)

internal fun SopletterDefaultMessagesResponseDto.toDomain(): SopletterDefaultMessages = SopletterDefaultMessages(
    topicId = topicId,
    title = title,
    totalCount = totalCount,
    nextCursor = nextCursor,
    hasNext = hasNext,
    messages = messages.map(SopletterMessageDto::toDomain),
)

internal fun SopletterMessageDto.toDomain(): SopletterMessage = SopletterMessage(
    messageId = messageId,
    previewContent = previewContent,
    colorCode = colorCode,
    rotationDegree = rotationDegree,
    shapeType = SopletterShapeType.from(shapeType),
)

internal fun SopletterMessageDetailResponseDto.toDomain(): SopletterMessageDetail = SopletterMessageDetail(
    messageId = messageId,
    topicId = topicId,
    authorNickname = authorNickname,
    content = content,
    createdAt = createdAt,
    likeCount = likeCount,
    likedByMe = likedByMe,
    mine = mine,
)

internal fun SopletterTopicDto.toDomain(): SopletterTopic = SopletterTopic(
    topicId = topicId,
    title = title,
)
