package org.sopt.official.data.source.api.auth

import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.AuthResponse
import org.sopt.official.data.model.response.LogOutResponse

interface RemoteAuthDataSource {
    suspend fun refresh(token: RefreshRequest): AuthResponse
    suspend fun withdraw()
    suspend fun logout(request: LogOutRequest): LogOutResponse
}
