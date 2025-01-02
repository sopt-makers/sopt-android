package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.SignInRequest
import org.sopt.official.data.auth.remote.response.SignInResponse
import org.sopt.official.domain.auth.model.SignInCode
import org.sopt.official.domain.auth.model.SignInResult

fun SignInCode.toRequest(): SignInRequest =
    SignInRequest(
        code = code,
        authPlatform = authPlatform
    )

fun SignInResponse.toDomain(): SignInResult =
    SignInResult(
        accessToken = accessToken,
        refreshToken = refreshToken
    )