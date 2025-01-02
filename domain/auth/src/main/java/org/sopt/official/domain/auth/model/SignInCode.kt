package org.sopt.official.domain.auth.model

data class SignInCode(
    val code: String,
    val authPlatform: String
)