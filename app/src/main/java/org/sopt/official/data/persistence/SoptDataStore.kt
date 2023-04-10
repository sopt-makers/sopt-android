package org.sopt.official.data.persistence

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoptDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val store = if (BuildConfig.DEBUG) {
        context.getSharedPreferences(DEBUG_FILE_NAME, Context.MODE_PRIVATE)
    } else {
        EncryptedSharedPreferences.create(
            BuildConfig.persistenceStoreName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
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

    companion object {
        const val DEBUG_FILE_NAME = "sopt_debug"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val PLAYGROUND_TOKEN = "pg_token"
    }
}
