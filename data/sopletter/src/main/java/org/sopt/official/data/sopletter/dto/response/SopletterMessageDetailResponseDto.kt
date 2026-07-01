package org.sopt.official.data.sopletter.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterMessageDetailResponseDto(
    @SerialName("messageId")
    val messageId: Long,
    @SerialName("topicId")
    val topicId: Long? = null,
    @SerialName("authorNickname")
    val authorNickname: String,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("likedByMe")
    val likedByMe: Boolean,
    @SerialName("mine")
    val mine: Boolean,
)
