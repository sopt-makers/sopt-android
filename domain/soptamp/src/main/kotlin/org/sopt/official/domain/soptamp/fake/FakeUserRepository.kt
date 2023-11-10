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
package org.sopt.official.domain.soptamp.fake

import org.sopt.officiail.domain.mypage.model.SoptampUser
import org.sopt.officiail.domain.mypage.repository.UserRepository

object FakeUserRepository : UserRepository {
    override val nickname: String = ""
    private val fakeUser = SoptampUser("", 1, "")

    override suspend fun checkNickname(nickname: String): Result<Unit> = runCatching {}

    override suspend fun logout(): Result<Unit> = runCatching {}
    override suspend fun getUserInfo(): Result<SoptampUser> = runCatching { fakeUser }

    override suspend fun updateProfileMessage(profileMessage: String): Result<Unit> = runCatching {}

    override suspend fun updateNickname(nickname: String): Result<Unit> = runCatching {}
    override fun updateLocalUserInfo(profileMessage: String) = Unit

    override fun fetchUserId() = 1
    override fun getIsOnboardingSeen() = false
    override fun updateOnboardingSeen(value: Boolean) = Unit
}
