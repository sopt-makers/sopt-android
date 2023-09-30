package org.sopt.official.data.model.notification.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSubscriptionRequest(
    @SerialName("isOptIn") val isOptIn: Boolean? = null,
)