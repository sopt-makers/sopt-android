package org.sopt.official.auth.impl.mapper

import org.sopt.official.auth.model.CentralizeToken
import org.sopt.official.network.model.request.ExpiredTokenRequest
import org.sopt.official.network.model.response.ValidTokenResponse

fun CentralizeToken.toRequest(): ExpiredTokenRequest =
    ExpiredTokenRequest(
        accessToken = accessToken,
        refreshToken = refreshToken
    )

fun ValidTokenResponse.toDomain(): CentralizeToken =
    CentralizeToken(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
