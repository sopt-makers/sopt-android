package org.sopt.official.data.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AuthRetrofit
import org.sopt.official.data.auth.remote.api.AuthApi
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    @Singleton
    internal fun provideAuthApi(@AuthRetrofit retrofit: Retrofit): AuthApi = retrofit.create()
}