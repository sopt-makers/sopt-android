package org.sopt.official.data.sopletter.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterDefaultMessagesResponseDto(
    @SerialName("topicId")
    val topicId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("nextCursor")
    val nextCursor: Long? = null,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("messages")
    val messages: List<SopletterMessageDto>,
)

@Serializable
data class SopletterMessageDto(
    @SerialName("messageId")
    val messageId: Long,
    @SerialName("previewContent")
    val previewContent: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("rotationDegree")
    val rotationDegree: Double,
    @SerialName("shapeType")
    val shapeType: String,
)
