package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSubscriptionResponse(
    val isOptIn: Boolean
)