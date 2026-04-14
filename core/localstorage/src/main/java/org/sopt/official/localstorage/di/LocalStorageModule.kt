package org.sopt.official.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.soptDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sopt_datastore",
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(context, "sampleKey")
        )
    }
)

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.soptDataStore
}