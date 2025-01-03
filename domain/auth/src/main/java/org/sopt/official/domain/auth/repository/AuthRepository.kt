package org.sopt.official.domain.auth.repository

import org.sopt.official.domain.auth.model.AccountResult
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.OriginalInformation
import org.sopt.official.domain.auth.model.SignInCode
import org.sopt.official.domain.auth.model.SignInResult
import org.sopt.official.domain.auth.model.SignUpCode
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.model.VerificationResult

interface AuthRepository {
    suspend fun createCode(request: InitialInformation): Result<Unit>

    suspend fun certificateCode(request: InformationWithCode): Result<VerificationResult>

    suspend fun signIn(request: SignInCode): Result<SignInResult>

    suspend fun signUp(request: SignUpCode): Result<Unit>

    suspend fun changeAccount(request: OriginalInformation): Result<Unit>

    suspend fun findAccount(request: UserPhoneNumber): Result<AccountResult>
}