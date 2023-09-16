package org.sopt.official.data.model.notification.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationHistoryItemResponse(
    @SerialName("")
    val list: ArrayList<NotificationHistoryItem>
)

@Serializable
data class NotificationHistoryItem(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String,
    val type: String?,
    val isRead: Boolean,
    val createdAt: String,
    val updatedAt: String,
)