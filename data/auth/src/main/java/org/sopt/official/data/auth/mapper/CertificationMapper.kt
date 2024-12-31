package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.CertificationNumberRequest
import org.sopt.official.domain.auth.model.UserInformation

fun UserInformation.toRequest(): CertificationNumberRequest =
    CertificationNumberRequest(
        name = name,
        phone = phone,
        type = type
    )