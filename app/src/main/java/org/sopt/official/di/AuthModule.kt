package org.sopt.official.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.common.di.Auth
import org.sopt.official.data.interceptor.AuthInterceptor
import org.sopt.official.data.repository.AuthRepositoryImpl
import org.sopt.official.data.service.AuthService
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.data.source.impl.DefaultRemoteAuthDataSource
import org.sopt.official.domain.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {
    @Provides
    @Singleton
    @Auth
    fun provideAuthService(@AppRetrofit retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    @Auth(false)
    fun provideNoneAuthService(@AppRetrofit(false) retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

        @Binds
        @Singleton
        @Auth
        fun bindAuthInterceptor(interceptor: AuthInterceptor): Interceptor

        @Binds
        @Singleton
        fun bindRemoteAuthDataSource(dataSource: DefaultRemoteAuthDataSource): RemoteAuthDataSource
    }
}
