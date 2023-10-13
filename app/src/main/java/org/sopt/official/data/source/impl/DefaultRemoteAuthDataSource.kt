package org.sopt.official.data.source.impl

import org.sopt.official.common.di.Auth
import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.AuthResponse
import org.sopt.official.data.model.response.LogOutResponse
import org.sopt.official.data.service.AuthService
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import javax.inject.Inject

class DefaultRemoteAuthDataSource @Inject constructor(
    @Auth private val service: AuthService,
    @Auth(false) private val noneAuthService: AuthService,
) : RemoteAuthDataSource {
    override suspend fun refresh(token: RefreshRequest): AuthResponse {
        return noneAuthService.refresh(token)
    }

    override suspend fun withdraw() {
        service.withdraw()
    }

    override suspend fun logout(request: LogOutRequest): LogOutResponse {
        return service.logOut(request)
    }
}
