package org.sopt.official.data.appjamtamp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.appjamtamp.service.AppjamtampService
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAppjamtampService(retrofit: Retrofit): AppjamtampService =
        retrofit.create()

}