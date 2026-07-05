package org.sopt.official.sopletter.model


data class SopletterWrite(
    val messageId: Long,
    val topicId: Long?,
    val authorNickname: String,
    val content: String,
    val colorCode: String,
    val rotationDegree: Double,
    val shapeType: String,
    val createdAt: String,
    val updatedAt: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val mine: Boolean
)