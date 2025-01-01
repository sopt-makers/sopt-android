package org.sopt.official.domain.auth.repository

import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.VerificationResult

interface AuthRepository {
    suspend fun createCode(request: InitialInformation): Result<Unit>

    suspend fun certificateCode(request: InformationWithCode): Result<VerificationResult>
}