package org.sopt.official.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.repository.AuthRepositoryImpl
import org.sopt.official.data.service.AuthService
import org.sopt.official.domain.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(repository: AuthRepositoryImpl): AuthRepository = repository
}
