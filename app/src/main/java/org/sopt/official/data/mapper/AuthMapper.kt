package org.sopt.official.data.mapper

import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.network.model.response.AuthResponse

class AuthMapper {
    fun toEntity(responseItem:AuthResponse) = Auth(
        Token(
            accessToken = responseItem.accessToken,
            refreshToken = responseItem.refreshToken,
            playgroundToken = responseItem.playgroundToken
        ),
        UserStatus.of(responseItem.status)
    )
}