package org.sopt.official.data.model.notification.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationHistoryItemResponse(
    @SerialName("notificationId")
    val notificationId: Long,
    @SerialName("userId")
    val userId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String?,
    @SerialName("category")
    val category: String,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
)