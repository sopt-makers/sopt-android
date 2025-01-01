package org.sopt.official.data.auth.repository

import org.sopt.official.data.auth.mapper.toDomain
import org.sopt.official.data.auth.mapper.toRequest
import org.sopt.official.data.auth.remote.api.AuthApi
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.VerificationResult
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun createCode(request: InitialInformation): Result<Unit> = runCatching {
        authApi.createCode(request.toRequest())
    }

    override suspend fun certificateCode(request: InformationWithCode): Result<VerificationResult> =
        runCatching {
            authApi.certificateCode(request.toRequest()).data.toDomain()
        }
}