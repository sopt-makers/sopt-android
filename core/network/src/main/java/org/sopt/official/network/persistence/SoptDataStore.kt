/*
 * MIT License
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.sopt.official.common.di.LocalStore
import org.sopt.official.common.file.createSharedPreference

@Singleton
class SoptDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    @LocalStore private val fileName: String,
) {
    private val store = createSharedPreference(fileName, context)

    /**
     * androidx.security.crypto.MasterKeys.ANDROID_KEYSTORE 참고
     */

    fun clear() {
        store.edit(true) {
            clear()
        }
    }

    var accessToken: String
        set(value) = store.edit { putString(ACCESS_TOKEN, value) }
        get() = store.getString(ACCESS_TOKEN, "") ?: ""

    var refreshToken: String
        set(value) = store.edit { putString(REFRESH_TOKEN, value) }
        get() = store.getString(REFRESH_TOKEN, "") ?: ""

    var playgroundToken: String
        set(value) = store.edit { putString(PLAYGROUND_TOKEN, value) }
        get() = store.getString(PLAYGROUND_TOKEN, "") ?: ""

    var userStatus: String
        set(value) = store.edit { putString(USER_STATUS, value) }
        get() = store.getString(USER_STATUS, UNAUTHENTICATED) ?: UNAUTHENTICATED

    var pushToken: String
        set(value) = store.edit { putString(PUSH_TOKEN, value) }
        get() = store.getString(PUSH_TOKEN, "") ?: ""

    companion object {
        const val DEBUG_FILE_NAME = "sopt_debug"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val PLAYGROUND_TOKEN = "pg_token"
        private const val USER_STATUS = "user_status"
        private const val KEY_ALIAS_AUTH = "alias.preferences.auth_token"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val PUSH_TOKEN = "push_token"
        private const val UNAUTHENTICATED = "UNAUTHENTICATED"
    }
}
