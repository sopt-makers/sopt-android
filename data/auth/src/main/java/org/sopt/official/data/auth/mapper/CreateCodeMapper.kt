package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.CreateCodeRequest
import org.sopt.official.domain.auth.model.InitialInformation

fun InitialInformation.toRequest(): CreateCodeRequest =
    CreateCodeRequest(
        name = name,
        phone = phone,
        type = type
    )