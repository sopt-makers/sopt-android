package org.sopt.official.data.sopletter.mapper

import org.sopt.official.data.sopletter.dto.SopletterWriteResponse
import org.sopt.official.sopletter.model.Sopletter

internal fun SopletterWriteResponse.toDomain(): Sopletter {
    return Sopletter(
        messageId = messageId,
        topicId = topicId,
        authorNickname = authorNickname,
        content = content,
        colorCode = colorCode,
        rotationDegree = rotationDegree,
        shapeType = shapeType,
        createdAt = createdAt,
        updatedAt = updatedAt,
        likeCount = likeCount,
        likedByMe = likedByMe,
        mine = mine
    )
}