/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.official.stamp.BuildConfig
import org.sopt.official.stamp.di.constant.Soptamp
import org.sopt.official.stamp.di.constant.Constant
import org.sopt.official.stamp.di.constant.Strings
import timber.log.Timber
import java.security.KeyStore
import javax.inject.Singleton

private const val KEY_ALIAS_AUTH = "alias.preferences.auth_token"
private const val ANDROID_KEY_STORE = "AndroidKeyStore"

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    @Soptamp
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
        @Strings(Constant.SOPTAMP_DATA_STORE) fileName: String
    ): SharedPreferences = try {
        createSharedPreference(!BuildConfig.DEBUG, fileName, context)
    } catch (e: Exception) {
        Timber.e(e)
        deleteMasterKeyEntry()
        deleteEncryptedPreference(context)
        createSharedPreference(!BuildConfig.DEBUG, fileName, context)
    }

    private fun createSharedPreference(
        isEncrypted: Boolean,
        fileName: String,
        context: Context
    ) = if (isEncrypted) {
        EncryptedSharedPreferences.create(
            context,
            fileName,
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } else {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    /**
     * androidx.security.crypto.MasterKeys.ANDROID_KEYSTORE 참고
     */
    private fun deleteMasterKeyEntry() {
        KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
            deleteEntry(KEY_ALIAS_AUTH)
        }
    }

    private fun deleteEncryptedPreference(context: Context) {
        context.deleteSharedPreferences(BuildConfig.persistenceStoreName)
    }
}

