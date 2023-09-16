package org.sopt.official.data.model.notification.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSubscriptionRequest(
    @SerialName("allOptIn") val allOptIn: Boolean? = null,
    @SerialName("partOptIn") val partOptIn: Boolean? = null,
    @SerialName("newsOptIn") val newsOptIn: Boolean? = null
)