package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.FindAccountRequest
import org.sopt.official.data.auth.remote.response.FindAccountResponse
import org.sopt.official.domain.auth.model.AccountResult
import org.sopt.official.domain.auth.model.UserPhoneNumber

fun UserPhoneNumber.toRequest(): FindAccountRequest =
    FindAccountRequest(
        phone = phone
    )

fun FindAccountResponse.toDomain(): AccountResult =
    AccountResult(
        platform = platform
    )
