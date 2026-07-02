/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.user.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import org.sopt.official.cache.InMemoryCache
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.user.mapper.toDomain
import org.sopt.official.data.user.remote.api.UserApi
import org.sopt.official.domain.user.model.UserInfo
import org.sopt.official.domain.user.model.WithdrawModel
import org.sopt.official.domain.user.repository.SoptUserRepository

/**
 * UserApi를 통해 사용자 정보를 가져와 캐싱하는 Repository
 * */
@Singleton
internal class DefaultUserRepository @Inject constructor(
    private val userApi: UserApi,
    private val cache: InMemoryCache<UserInfo>,
) : SoptUserRepository {

    override val userInfo: StateFlow<UserInfo?> = cache.data

    override suspend fun getUserInfo(): Result<UserInfo> =
        suspendRunCatching { cache.getOrFetch { userApi.getUserMain().toDomain() } }

    override suspend fun refreshUserInfo(): Result<UserInfo> = suspendRunCatching {
        cache.invalidate()
        cache.getOrFetch { userApi.getUserMain().toDomain() }
    }

    override suspend fun withdraw(): Result<WithdrawModel> = suspendRunCatching {
        cache.invalidate()
        userApi.withdraw().toDomain()
    }

    override suspend fun invalidate() {
        cache.invalidate()
    }
}
