package org.sopt.official.data.sopletter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.sopletter.service.SopletterService
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideSopletterService(
        @AppRetrofit(true) retrofit: Retrofit,
    ): SopletterService = retrofit.create(SopletterService::class.java)
}
