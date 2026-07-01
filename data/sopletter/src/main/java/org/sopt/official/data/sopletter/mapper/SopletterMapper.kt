package org.sopt.official.data.sopletter.mapper
import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDto
import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
import org.sopt.official.domain.sopletter.model.SopletterMessageDetail
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.domain.sopletter.model.SopletterShapeType

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
