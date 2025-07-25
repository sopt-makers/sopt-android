package org.sopt.official.common.config.remoteconfig

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

internal const val UPDATE_CONFIG = "android_update_notice"

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigProvidesModule {
    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(mapOf(UPDATE_CONFIG to ""))
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigBindsModule {
    @Binds
    @Singleton
    abstract fun bindRemoteConfig(remoteConfigImpl: SoptRemoteConfigImpl): SoptRemoteConfig
}
