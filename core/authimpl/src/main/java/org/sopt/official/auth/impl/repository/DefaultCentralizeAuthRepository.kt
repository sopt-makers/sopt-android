/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.auth.impl.repository

import dev.zacsweers.metro.Inject
import org.sopt.official.auth.impl.mapper.toDomain
import org.sopt.official.auth.impl.mapper.toRequest
import org.sopt.official.auth.model.CentralizeToken
import org.sopt.official.auth.repository.CentralizeAuthRepository
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.network.service.RefreshApi

@Inject
class DefaultCentralizeAuthRepository(
    private val refreshApi: RefreshApi,
    private val dataStore: SoptDataStore
) : CentralizeAuthRepository {
    override suspend fun refreshToken(expiredToken: CentralizeToken): Result<CentralizeToken> =
        suspendRunCatching {
            val newTokens = refreshApi.refreshToken(expiredToken.toRequest()).data!!.toDomain()

            dataStore.accessToken = newTokens.accessToken
            dataStore.refreshToken = newTokens.refreshToken

            newTokens
        }
}
