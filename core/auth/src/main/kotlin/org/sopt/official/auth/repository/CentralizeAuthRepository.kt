package org.sopt.official.auth.repository

import org.sopt.official.auth.model.CentralizeToken

interface CentralizeAuthRepository {
    suspend fun refreshToken(expiredToken: CentralizeToken): Result<CentralizeToken>
}