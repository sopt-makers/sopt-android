/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.network.persistence

import android.content.Context
import androidx.core.content.edit
import org.sopt.official.common.di.LocalStore
import org.sopt.official.common.file.createSharedPreference
import org.sopt.official.common.util.decryptInReleaseMode
import org.sopt.official.common.util.encryptInReleaseMode
import org.sopt.official.network.BuildConfig.ACCESS_TOKEN_KEY_ALIAS
import org.sopt.official.network.BuildConfig.PLAYGROUND_TOKEN_KEY_ALIAS
import org.sopt.official.network.BuildConfig.PUSH_TOKEN_KEY_ALIAS
import org.sopt.official.network.BuildConfig.REFRESH_TOKEN_KEY_ALIAS
import org.sopt.official.network.BuildConfig.USER_STATUS_KEY_ALIAS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoptDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    @LocalStore private val fileName: String
) {
    private val store = createSharedPreference(fileName, context)

    fun clear() {
        store.edit(true) {
            clear()
        }
    }

    var accessToken: String
        set(value) = store.edit { putString(ACCESS_TOKEN, value.encryptInReleaseMode(keyAlias = ACCESS_TOKEN_KEY_ALIAS)) }
        get() = store.getString(ACCESS_TOKEN, null)?.decryptInReleaseMode(keyAlias = ACCESS_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE

    var refreshToken: String
        set(value) = store.edit { putString(REFRESH_TOKEN, value.encryptInReleaseMode(keyAlias = REFRESH_TOKEN_KEY_ALIAS)) }
        get() = store.getString(REFRESH_TOKEN, null)?.decryptInReleaseMode(keyAlias = REFRESH_TOKEN_KEY_ALIAS)
            ?: DEFAULT_VALUE

    var playgroundToken: String
        set(value) = store.edit { putString(PLAYGROUND_TOKEN, value.encryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS)) }
        get() = store.getString(PLAYGROUND_TOKEN, null)?.decryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS)
            ?: DEFAULT_VALUE

    var userStatus: String
        set(value) = store.edit { putString(USER_STATUS, value.encryptInReleaseMode(keyAlias = USER_STATUS_KEY_ALIAS)) }
        get() = store.getString(USER_STATUS, null)?.decryptInReleaseMode(keyAlias = USER_STATUS_KEY_ALIAS) ?: UNAUTHENTICATED

    var pushToken: String
        set(value) = store.edit { putString(PUSH_TOKEN, value.encryptInReleaseMode(keyAlias = PUSH_TOKEN_KEY_ALIAS)) }
        get() = store.getString(PUSH_TOKEN, null)?.decryptInReleaseMode(keyAlias = PUSH_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE

    var platform: String
        set(value) = store.edit { putString(PLATFORM, value.encryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS)) }
        get() = store.getString(PLATFORM, null)?.decryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val PLAYGROUND_TOKEN = "pg_token"
        private const val USER_STATUS = "user_status"
        private const val PUSH_TOKEN = "push_token"
        private const val UNAUTHENTICATED = "UNAUTHENTICATED"
        private const val DEFAULT_VALUE = ""
        private const val PLATFORM = "platform"
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SoptDataStoreEntryPoint {
    fun soptDataStore(): SoptDataStore
}
