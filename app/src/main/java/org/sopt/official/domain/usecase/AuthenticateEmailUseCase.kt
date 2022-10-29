package org.sopt.official.domain.usecase

import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, clientToken: String) = runCatching {
        val userId = authRepository.authenticateEmail(email, clientToken)
        authRepository.saveUserToken(userId)
    }
}
