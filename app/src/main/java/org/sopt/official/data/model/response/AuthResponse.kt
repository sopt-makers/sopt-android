package org.sopt.official.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.Token

@Serializable
data class AuthResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("playgroundToken") val playgroundToken: String
) {
    fun toEntity() = Token(
        accessToken = accessToken,
        refreshToken = refreshToken,
        playgroundToken = playgroundToken
    )
}
