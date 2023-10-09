package org.sopt.official.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.repository.notfication.NotificationRepositoryImpl
import org.sopt.official.data.service.notification.NotificationService
import org.sopt.official.domain.repository.notification.NotificationRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NotificationModule {

    @Provides
    @Singleton
    fun provideFcmService(
        @AppRetrofit retrofit: Retrofit
    ): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun bindNotificationRepository(repository: NotificationRepositoryImpl): NotificationRepository
    }
}
