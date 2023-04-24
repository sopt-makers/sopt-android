package org.sopt.official.data.repository

import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val dataStore: SoptDataStore
) : AuthRepository {
    override suspend fun refresh(token: String) = runCatching {
        service.refresh(RefreshRequest(token)).toEntity()
    }

    override fun save(token: Token) {
        dataStore.apply {
            accessToken = token.accessToken
            refreshToken = token.refreshToken
            playgroundToken = token.playgroundToken
        }
    }

    override fun save(status: UserStatus) {
        dataStore.apply {
            userStatus = status.value
        }
    }

    override suspend fun withdraw() = runCatching {
        service.withdraw()
    }

    override suspend fun logout() = runCatching {
        dataStore.clear()
    }
}
