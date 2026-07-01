package org.sopt.official.domain.sopletter.model

data class SopletterMessageDetail(
    val messageId: Long,
    val topicId: Long?,
    val authorNickname: String,
    val content: String,
    val createdAt: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val mine: Boolean,
)
