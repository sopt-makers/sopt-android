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
package org.sopt.official.localstorage.sourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.sopt.official.common.BuildConfig.ACCESS_TOKEN_KEY_ALIAS
import org.sopt.official.common.BuildConfig.PLAYGROUND_TOKEN_KEY_ALIAS
import org.sopt.official.common.BuildConfig.PUSH_TOKEN_KEY_ALIAS
import org.sopt.official.common.BuildConfig.REFRESH_TOKEN_KEY_ALIAS
import org.sopt.official.common.BuildConfig.USER_STATUS_KEY_ALIAS
import org.sopt.official.common.util.decryptInReleaseMode
import org.sopt.official.common.util.encryptInReleaseMode
import org.sopt.official.localstorage.source.GlobalStorage
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus
import javax.inject.Inject

class DefaultSoptStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenStorage, UserStorage, GlobalStorage {
    override val accessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_ACCESS_TOKEN]?.decryptInReleaseMode(keyAlias = ACCESS_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE
    }

    override val refreshToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_REFRESH_TOKEN]?.decryptInReleaseMode(keyAlias = REFRESH_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE
    }

    override val playgroundToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_PLAYGROUND_TOKEN]?.decryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE
    }

    override val isSopletterOnboardingCompleted: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[KEY_ONBOARDING_COMPLETED]
    }

    override val isAppjamMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_IS_APPJAM_MODE] ?: false
    }

    override val isNotificationPermissionRequested: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_NOTIFICATION_PERMISSION_REQUESTED] ?: false
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = accessToken.encryptInReleaseMode(keyAlias = ACCESS_TOKEN_KEY_ALIAS)
            preferences[KEY_REFRESH_TOKEN] = refreshToken.encryptInReleaseMode(keyAlias = REFRESH_TOKEN_KEY_ALIAS)
        }
    }

    override suspend fun savePlaygroundToken(playgroundToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PLAYGROUND_TOKEN] = playgroundToken.encryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS)
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_PLAYGROUND_TOKEN)
        }
    }

    override val userStatus: Flow<UserStatus> = dataStore.data.map { preferences ->
        val statusString = preferences[KEY_USER_STATUS]?.decryptInReleaseMode(keyAlias = USER_STATUS_KEY_ALIAS) ?: UNAUTHENTICATED
        UserStatus.of(statusString)
    }

    override val pushToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_PUSH_TOKEN]?.decryptInReleaseMode(keyAlias = PUSH_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE
    }

    override val platform: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_PLATFORM]?.decryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS) ?: DEFAULT_VALUE
    }

    override suspend fun saveUserStatus(status: UserStatus) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_STATUS] = status.value.encryptInReleaseMode(keyAlias = USER_STATUS_KEY_ALIAS)
        }
    }

    override suspend fun savePushToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PUSH_TOKEN] = token.encryptInReleaseMode(keyAlias = PUSH_TOKEN_KEY_ALIAS)
        }
    }

    override suspend fun savePlatform(platform: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PLATFORM] = platform.encryptInReleaseMode(keyAlias = PLAYGROUND_TOKEN_KEY_ALIAS)
        }
    }

    override suspend fun saveOnboardingCompleted(isOnboarded: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_ONBOARDING_COMPLETED] = isOnboarded
        }
    }

    override suspend fun saveIsAppjamMode(isAppjamMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_APPJAM_MODE] = isAppjamMode
        }
    }

    override suspend fun saveNotificationPermissionRequested(isRequested: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATION_PERMISSION_REQUESTED] = isRequested
        }
    }

    override suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_USER_STATUS)
            preferences.remove(KEY_PUSH_TOKEN)
            preferences.remove(KEY_PLATFORM)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences ->
            val isNotificationPermissionRequested = preferences[KEY_NOTIFICATION_PERMISSION_REQUESTED]
            preferences.clear()
            isNotificationPermissionRequested?.let {
                preferences[KEY_NOTIFICATION_PERMISSION_REQUESTED] = it
            }
        }
    }

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_PLAYGROUND_TOKEN = stringPreferencesKey("pg_token")
        private val KEY_USER_STATUS = stringPreferencesKey("user_status")
        private val KEY_PUSH_TOKEN = stringPreferencesKey("push_token")
        private val KEY_PLATFORM = stringPreferencesKey("platform")
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_IS_APPJAM_MODE = booleanPreferencesKey("is_appjam_mode")
        private val KEY_NOTIFICATION_PERMISSION_REQUESTED = booleanPreferencesKey("notification_permission_requested")


        private const val DEFAULT_VALUE = ""
        private const val UNAUTHENTICATED = "UNAUTHENTICATED"
    }
}
