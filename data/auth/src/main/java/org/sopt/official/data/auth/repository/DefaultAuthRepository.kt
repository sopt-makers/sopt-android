package org.sopt.official.data.auth.repository

import org.sopt.official.data.auth.mapper.toRequest
import org.sopt.official.data.auth.remote.api.AuthApi
import org.sopt.official.domain.auth.model.UserInformation
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun getCertificationNumber(request: UserInformation): Result<Unit> = runCatching {
        authApi.postCertificationNumber(request.toRequest())
    }
}