package org.sopt.official.domain.auth.model

data class SignInCode(
    val token: String,
    val authPlatform: String
)