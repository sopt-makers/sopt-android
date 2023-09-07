package org.sopt.official.util

import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus

fun OAuthToken.toEntity() = Auth(
    Token(
        accessToken = accessToken,
        refreshToken = refreshToken,
        playgroundToken = playgroundToken
    ),
    status = UserStatus.valueOf(status)
)