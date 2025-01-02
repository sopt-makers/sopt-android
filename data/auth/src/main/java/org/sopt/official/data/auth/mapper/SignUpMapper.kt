package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.SignUpRequest
import org.sopt.official.domain.auth.model.SignUpCode

fun SignUpCode.toRequest(): SignUpRequest =
    SignUpRequest(
        name = name,
        phone = phone,
        code = code,
        authPlatform = authPlatform
    )