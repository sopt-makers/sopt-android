package org.sopt.official.localstorage.sourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    override suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_USER_STATUS)
            preferences.remove(KEY_PUSH_TOKEN)
            preferences.remove(KEY_PLATFORM)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_PLAYGROUND_TOKEN = stringPreferencesKey("pg_token")
        private val KEY_USER_STATUS = stringPreferencesKey("user_status")
        private val KEY_PUSH_TOKEN = stringPreferencesKey("push_token")
        private val KEY_PLATFORM = stringPreferencesKey("platform")


        private const val DEFAULT_VALUE = ""
        private const val UNAUTHENTICATED = "UNAUTHENTICATED"
    }
}