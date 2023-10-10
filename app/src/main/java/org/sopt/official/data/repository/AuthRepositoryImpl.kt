package org.sopt.official.data.repository

import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.LogOutResponse
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: RemoteAuthDataSource,
    private val dataStore: SoptDataStore
) : AuthRepository {
    override suspend fun refresh(token: String) = runCatching {
        authDataSource.refresh(RefreshRequest(token)).toEntity()
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
        authDataSource.withdraw()
    }

    override suspend fun logout(
        pushToken: String
    ): Result<LogOutResponse> = runCatching {
        authDataSource.logout(
            LogOutRequest(
                platform = "Android",
                pushToken = pushToken
            )
        )
    }
}
