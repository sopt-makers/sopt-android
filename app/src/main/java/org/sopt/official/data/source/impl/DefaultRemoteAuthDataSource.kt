/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
