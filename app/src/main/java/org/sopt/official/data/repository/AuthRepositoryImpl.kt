package org.sopt.official.data.repository

import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val dataStore: SoptDataStore
) : AuthRepository {
    override suspend fun authenticate(code: String) =
        service.authenticate(AuthRequest(code)).toEntity().first

    override fun save(token: Token) {
        dataStore.apply {
            accessToken = token.accessToken
            refreshToken = token.refreshToken
            playgroundToken = token.playgroundToken
        }
    }
}
