package org.sopt.official.data.sopletter.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SopletterWriteResponse(
    @SerialName("messageId")
    val messageId: Long,
    @SerialName("topicId")
    val topicId: Long? = null,
    @SerialName("authorNickname")
    val authorNickname: String,
    @SerialName("content")
    val content: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("rotationDegree")
    val rotationDegree: Double,
    @SerialName("shapeType")
    val shapeType: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("likedByMe")
    val likedByMe: Boolean,
    @SerialName("mine")
    val mine: Boolean
)