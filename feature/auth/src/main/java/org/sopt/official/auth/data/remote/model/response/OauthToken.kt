package org.sopt.official.auth.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OAuthToken(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("playgroundToken") val playgroundToken: String,
    @SerialName("status") val status: String
)
