package org.sopt.official.data.sopletter.mapper

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDto
import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
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
