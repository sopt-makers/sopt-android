package org.sopt.official.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogOutRequest(
    @SerialName("platform") val platform: String,
    @SerialName("pushToken") val pushToken: String
)
