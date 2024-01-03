package org.sopt.official.data.poke.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.poke.BuildConfig
import timber.log.Timber
import java.security.KeyStore
import javax.inject.Singleton

private const val KEY_ALIAS_AUTH = "alias.preferences.poke"
private const val ANDROID_KEY_STORE = "AndroidKeyStore"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    @Poke
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
        @Strings(Constant.POKE_DATA_STORE) fileName: String
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
        context.deleteSharedPreferences("sampleKey")
    }
}
