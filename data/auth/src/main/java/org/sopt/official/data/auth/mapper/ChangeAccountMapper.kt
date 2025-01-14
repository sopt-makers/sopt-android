package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.ChangeAccountRequest
import org.sopt.official.domain.auth.model.OriginalInformation

fun OriginalInformation.toRequest(): ChangeAccountRequest =
    ChangeAccountRequest(
        phone = phone,
        authPlatform = authPlatform,
        token = token
    )

