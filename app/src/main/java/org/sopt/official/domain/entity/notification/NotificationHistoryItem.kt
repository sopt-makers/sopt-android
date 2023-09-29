package org.sopt.official.domain.entity.notification

data class NotificationHistoryItem(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String,
    val type: String?,
    var isRead: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
