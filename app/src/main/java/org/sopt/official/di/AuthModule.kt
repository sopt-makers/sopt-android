/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.sopt.official.data.source.api.auth.LocalAuthDataSource
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.data.source.impl.DefaultLocalAuthDataSource
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

        @Binds
        @Singleton
        fun bindLocalAuthDataSource(dataSource: DefaultLocalAuthDataSource): LocalAuthDataSource
    }
}
