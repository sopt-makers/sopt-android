package org.sopt.official.data.auth.repository

import org.sopt.official.data.auth.mapper.toDomain
import org.sopt.official.data.auth.mapper.toRequest
import org.sopt.official.data.auth.remote.api.AuthApi
import org.sopt.official.domain.auth.model.AccountResult
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.OriginalInformation
import org.sopt.official.domain.auth.model.SignInCode
import org.sopt.official.domain.auth.model.SignUpCode
import org.sopt.official.domain.auth.model.Token
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.model.VerificationResult
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun createCode(request: InitialInformation): Result<Unit> = runCatching {
        authApi.createCode(request.toRequest())
    }

    override suspend fun certificateCode(request: InformationWithCode): Result<VerificationResult> = runCatching {
        authApi.certificateCode(request.toRequest()).data.toDomain()
    }

    override suspend fun signIn(request: SignInCode): Result<Token> = runCatching {
        authApi.signIn(request.toRequest()).data.toDomain()
    }

    override suspend fun signUp(request: SignUpCode): Result<Unit> = runCatching {
        authApi.signUp(request.toRequest())
    }

    override suspend fun changeAccount(request: OriginalInformation): Result<Unit> = runCatching {
        authApi.changeAccount(request.toRequest())
    }

    override suspend fun findAccount(request: UserPhoneNumber): Result<AccountResult> = runCatching {
        authApi.findAccount(request.toRequest()).data.toDomain()
    }
}