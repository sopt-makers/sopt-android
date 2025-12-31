package org.sopt.official.data.appjamtamp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.appjamtamp.service.AppjamtampService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideAppjamtampService(@AppRetrofit(true) retrofit: Retrofit): AppjamtampService =
        retrofit.create(AppjamtampService::class.java)
}