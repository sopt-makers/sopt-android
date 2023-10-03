package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationHistoryItemResponse(
    val notificationId: Int,
    val userId: Int,
    val title: String,
    val content: String?,
    val category: String,
    val isRead: Boolean,
    val createdAt: String,
)