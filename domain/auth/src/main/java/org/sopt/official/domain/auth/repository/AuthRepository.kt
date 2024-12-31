package org.sopt.official.domain.auth.repository

import org.sopt.official.domain.auth.model.UserInformation

interface AuthRepository {
    suspend fun getCertificationNumber(request: UserInformation) : Result<Unit>
}