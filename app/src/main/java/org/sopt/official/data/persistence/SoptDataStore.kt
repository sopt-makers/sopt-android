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
    private val store = if (BuildConfig.DEBUG) context.getSharedPreferences(DEBUG_FILE_NAME, Context.MODE_PRIVATE)
    else EncryptedSharedPreferences.create(
        BuildConfig.persistenceStoreName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var userId: Long
        set(value) = store.edit { putLong("USER_TOKEN", value) }
        get() = store.getLong("USER_TOKEN", -1L)

    companion object {
        const val DEBUG_FILE_NAME = "sopt_debug"
    }
}
