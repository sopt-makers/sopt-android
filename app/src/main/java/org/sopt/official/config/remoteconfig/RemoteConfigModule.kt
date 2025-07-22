package org.sopt.official.config.remoteconfig

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): SoptRemoteConfig = SoptRemoteConfig()
}
