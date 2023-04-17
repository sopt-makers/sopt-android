package org.sopt.official.domain.usecase

import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(auth: Auth) {
        authRepository.save(auth.token)
        authRepository.save(auth.status)
    }
}
