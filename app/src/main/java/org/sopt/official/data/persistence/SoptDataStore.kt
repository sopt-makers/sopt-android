package org.sopt.official.data.persistence

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.BuildConfig
import org.sopt.official.domain.entity.auth.UserStatus
import timber.log.Timber
import java.security.KeyStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoptDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val store = try {
        createSharedPreference(!BuildConfig.DEBUG)
    } catch (e: Exception) {
        Timber.e(e)
        deleteMasterKeyEntry()
        deleteEncryptedPreference()
        createSharedPreference(!BuildConfig.DEBUG)
    }

    private fun createSharedPreference(isEncrypted: Boolean) = if (isEncrypted) {
        EncryptedSharedPreferences.create(
            BuildConfig.persistenceStoreName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } else {
        context.getSharedPreferences(DEBUG_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * androidx.security.crypto.MasterKeys.ANDROID_KEYSTORE 참고
     */
    private fun deleteMasterKeyEntry() {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
            deleteEntry(KEY_ALIAS_AUTH)
        }
    }

    private fun deleteEncryptedPreference() {
        context.deleteSharedPreferences(BuildConfig.persistenceStoreName)
    }

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
        get() = store.getString(USER_STATUS, UserStatus.UNAUTHENTICATED.value) ?: UserStatus.UNAUTHENTICATED.value

    companion object {
        const val DEBUG_FILE_NAME = "sopt_debug"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val PLAYGROUND_TOKEN = "pg_token"
        private const val USER_STATUS = "user_status"
        private const val KEY_ALIAS_AUTH = "alias.preferences.auth_token"
    }
}
