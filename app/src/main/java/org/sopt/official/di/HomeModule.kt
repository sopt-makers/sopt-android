package org.sopt.official.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.repository.home.DefaultHomeRepository
import org.sopt.official.data.service.home.HomeService
import org.sopt.official.domain.repository.home.HomeRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideMainViewService(
        @AppRetrofit retrofit: Retrofit
    ): HomeService = retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(repository: DefaultHomeRepository): HomeRepository = repository
}