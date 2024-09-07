package org.sopt.official.domain.notification.entity

data class NotificationItem(
    val notificationId: String,
    val userId: Int,
    val title: String,
    val content: String?,
    val category: String,
    var isRead: Boolean,
    val createdAt: String,
)
