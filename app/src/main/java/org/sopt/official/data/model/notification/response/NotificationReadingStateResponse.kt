package org.sopt.official.data.model.notification.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationReadingStateResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)