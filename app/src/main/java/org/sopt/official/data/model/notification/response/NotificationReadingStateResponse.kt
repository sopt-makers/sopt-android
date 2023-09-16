package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationReadingStateResponse(
    val id: Int,
    val isRead: Boolean,
    val createdAt: String,
    val updatedAt: String
)