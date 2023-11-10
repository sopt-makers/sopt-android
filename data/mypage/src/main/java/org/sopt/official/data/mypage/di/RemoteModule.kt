package org.sopt.official.data.mypage.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.mypage.remote.api.SoptampUserService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {
    @Provides
    @Singleton
    fun provideUserService(
        @AppRetrofit(true) retrofit: Retrofit
    ): SoptampUserService = retrofit.create(SoptampUserService::class.java)
}