package org.sopt.official.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus

@Serializable
data class AuthResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("playgroundToken") val playgroundToken: String,
    @SerialName("status") val status: String
) {
    fun toEntity() = Auth(
        Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
            playgroundToken = playgroundToken
        ),
        UserStatus.of(status)
    )

    fun toOAuthToken() = OAuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        playgroundToken = playgroundToken,
        status = status
    )
}
