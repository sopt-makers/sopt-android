package org.sopt.official.domain.entity.auth

data class Auth(
    val token: Token,
    val status: UserStatus
)
