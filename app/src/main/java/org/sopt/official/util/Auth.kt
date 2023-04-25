package org.sopt.official.util

import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.playground.auth.data.remote.model.response.OAuthToken

fun OAuthToken.toEntity() = Auth(
    Token(
        accessToken = accessToken,
        refreshToken = refreshToken,
        playgroundToken = playgroundToken
    ),
    status = UserStatus.valueOf(status)
)