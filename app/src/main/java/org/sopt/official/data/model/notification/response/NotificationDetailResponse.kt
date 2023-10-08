package org.sopt.official.data.model.notification.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetailResponse(
    @SerialName("notificationId")
    val notificationId: Int,
    @SerialName("userId")
    val userId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String?,
    @SerialName("deepLink")
    val deepLink: String?,
    @SerialName("webLink")
    val webLink: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)