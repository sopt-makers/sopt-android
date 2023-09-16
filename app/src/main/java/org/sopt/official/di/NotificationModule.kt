package org.sopt.official.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.core.di.AppRetrofit
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

    @Provides
    @Singleton
    fun provideNotificationRepository(
        repository: NotificationRepositoryImpl
    ): NotificationRepository {
        return repository
    }
}
