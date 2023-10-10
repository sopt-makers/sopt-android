package org.sopt.official.data.repository

import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.LogOutResponse
import org.sopt.official.data.source.api.auth.LocalAuthDataSource
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val localAuthDataSource: LocalAuthDataSource,
) : AuthRepository {
    override suspend fun refresh(token: String) = runCatching {
        remoteAuthDataSource.refresh(RefreshRequest(token)).toEntity()
    }

    override fun save(token: Token) {
        localAuthDataSource.save(token)
    }

    override fun save(status: UserStatus) {
        localAuthDataSource.save(status)
    }

    override suspend fun withdraw() = runCatching {
        remoteAuthDataSource.withdraw()
    }

    override suspend fun logout(
        pushToken: String
    ): Result<LogOutResponse> = runCatching {
        remoteAuthDataSource.logout(
            LogOutRequest(
                platform = "Android",
                pushToken = pushToken
            )
        )
    }
}
