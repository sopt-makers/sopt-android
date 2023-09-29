package org.sopt.official.data.model.notification.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenRequest(
    @SerialName("platform") val platform: String = "Android",
    @SerialName("pushToken") val pushToken: String
)