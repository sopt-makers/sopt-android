package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class UnreadNotificationExistenceResponse(
    val exists: Boolean
)