package org.sopt.official.data.schedule.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.schedule.api.ScheduleApi
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    internal fun provideScheduleApi(@AppRetrofit(true) retrofit: Retrofit): ScheduleApi = retrofit.create()
}
