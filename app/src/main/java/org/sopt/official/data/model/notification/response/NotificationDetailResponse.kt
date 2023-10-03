package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetailResponse(
    val notificationId: Int,
    val userId: Int,
    val title: String,
    val content: String?,
    val deepLink: String?,
    val webLink: String?,
    val createdAt: String,
    val updatedAt: String,
)