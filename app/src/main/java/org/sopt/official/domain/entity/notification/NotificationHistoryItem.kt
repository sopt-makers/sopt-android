package org.sopt.official.domain.entity.notification

data class NotificationHistoryItem(
    val notificationId: Long,
    val userId: Int,
    val title: String,
    val content: String?,
    val category: String,
    var isRead: Boolean,
    val createdAt: String,
)
