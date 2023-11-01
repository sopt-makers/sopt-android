package org.sopt.official.data.mapper

import org.sopt.official.network.model.AuthResponse

fun toAuthResponse(responseItem: AuthResponse): org.sopt.official.data.model.response.AuthResponse {
    return org.sopt.official.data.model.response.AuthResponse(
        accessToken = responseItem.accessToken,
        refreshToken = responseItem.refreshToken,
        playgroundToken = responseItem.playgroundToken,
        status = responseItem.status
    )
}

