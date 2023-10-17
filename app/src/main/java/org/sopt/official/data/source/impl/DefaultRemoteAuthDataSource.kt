/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
