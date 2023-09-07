package org.sopt.official.auth.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestToken(
    @SerialName("code") val code: String,
    @SerialName("pushToken") val pushToken: String
)
