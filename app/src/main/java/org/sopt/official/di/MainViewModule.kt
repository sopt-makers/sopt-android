package org.sopt.official.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.repository.main.MainViewRepositoryImpl
import org.sopt.official.data.service.main.MainViewService
import org.sopt.official.di.annotation.AppRetrofit
import org.sopt.official.domain.repository.main.MainViewRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainViewModule {
    @Provides
    @Singleton
    fun provideMainViewService(@AppRetrofit retrofit: Retrofit): MainViewService = retrofit.create(MainViewService::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(repository: MainViewRepositoryImpl): MainViewRepository = repository
}