/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.auth.impl.local

import org.sopt.official.auth.impl.source.LocalAuthDataSource
import org.sopt.official.auth.model.Token
import org.sopt.official.localstorage.source.GlobalStorage
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus
import javax.inject.Inject

class DefaultLocalAuthDataSource @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val userStorage: UserStorage,
    private val globalStorage: GlobalStorage
) : LocalAuthDataSource {
    override suspend fun save(token: Token) {
        tokenStorage.saveTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken
        )
        tokenStorage.savePlaygroundToken(token.playgroundToken)
    }

    override suspend fun save(status: UserStatus) {
        userStorage.saveUserStatus(status)
    }

    override suspend fun clear() {
        globalStorage.clearAll()
    }
}
