package org.sopt.official.data.auth.mapper

import org.sopt.official.data.auth.remote.request.CertificateCodeRequest
import org.sopt.official.data.auth.remote.response.CertificateCodeResponse
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.VerificationResult

fun InformationWithCode.toRequest(): CertificateCodeRequest =
    CertificateCodeRequest(
        name = name,
        phone = phone,
        code = code,
        type = type
    )

fun CertificateCodeResponse.toDomain() : VerificationResult =
    VerificationResult(
        isVerified = isVerified,
        name = name,
        phone = phone
    )