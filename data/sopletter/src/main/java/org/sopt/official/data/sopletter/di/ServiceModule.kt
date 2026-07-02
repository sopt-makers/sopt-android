package org.sopt.official.data.sopletter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.sopletter.api.SopletterService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    @Singleton
    fun provideSopletterService(
        @AppRetrofit retrofit: Retrofit
    ): SopletterService {
        return retrofit.create(SopletterService::class.java)
    }
}