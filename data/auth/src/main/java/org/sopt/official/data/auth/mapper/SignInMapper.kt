package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.SignInRequest
import org.sopt.official.data.auth.remote.response.SignInResponse
import org.sopt.official.domain.auth.model.SignInCode
import org.sopt.official.domain.auth.model.Token

fun SignInCode.toRequest(): SignInRequest =
    SignInRequest(
        token = token,
        authPlatform = authPlatform
    )

fun SignInResponse.toDomain(): Token =
    Token(
        accessToken = accessToken,
        refreshToken = refreshToken
    )