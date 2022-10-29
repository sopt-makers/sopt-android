package org.sopt.official.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAuthEmail(
    @SerialName("email") val email: String,
    @SerialName("clientToken") val clientToken: String,
    @SerialName("osType") val osType: String = "Android"
)
