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
package org.sopt.official.data.soptamp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.security.KeyStore
import javax.inject.Singleton
import org.sopt.official.data.soptamp.BuildConfig
import org.sopt.official.domain.soptamp.constant.Constant
import org.sopt.official.domain.soptamp.constant.Soptamp
import org.sopt.official.domain.soptamp.constant.Strings
import timber.log.Timber

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
        createSharedPreference(false, fileName, context)
    } catch (e: Exception) {
        Timber.e(e)
        deleteMasterKeyEntry()
        deleteEncryptedPreference(context)
        createSharedPreference(false, fileName, context)
    }

    private fun createSharedPreference(isEncrypted: Boolean, fileName: String, context: Context) = if (isEncrypted) {
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
        context.deleteSharedPreferences("sampleKey")
    }
}
