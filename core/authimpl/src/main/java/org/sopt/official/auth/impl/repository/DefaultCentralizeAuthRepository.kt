package org.sopt.official.auth.impl.repository

import org.sopt.official.auth.impl.mapper.toDomain
import org.sopt.official.auth.impl.mapper.toRequest
import org.sopt.official.auth.model.CentralizeToken
import org.sopt.official.auth.repository.CentralizeAuthRepository
import org.sopt.official.network.service.RefreshApi
import javax.inject.Inject

class DefaultCentralizeAuthRepository @Inject constructor(
    private val refreshApi: RefreshApi
) : CentralizeAuthRepository {
    override suspend fun refreshToken(expiredToken: CentralizeToken): Result<CentralizeToken> = runCatching {
        refreshApi.refreshToken(expiredToken.toRequest()).data?.toDomain() ?: CentralizeToken.setExpiredToken()
    }
}