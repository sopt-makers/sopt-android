/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.auth.impl.di

import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import org.sopt.official.auth.impl.api.AuthService
import org.sopt.official.auth.impl.local.DefaultLocalAuthDataSource
import org.sopt.official.auth.impl.remote.DefaultRemoteAuthDataSource
import org.sopt.official.auth.impl.repository.AuthRepositoryImpl
import org.sopt.official.auth.impl.repository.DefaultCentralizeAuthRepository
import org.sopt.official.auth.impl.source.LocalAuthDataSource
import org.sopt.official.auth.impl.source.RemoteAuthDataSource
import org.sopt.official.auth.repository.AuthRepository
import org.sopt.official.auth.repository.CentralizeAuthRepository
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.di.Auth
import retrofit2.Retrofit

@ContributesTo(AppScope::class)
@BindingContainer
interface AuthModule {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        @Auth
        fun provideAuthService(@AppRetrofit retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
    }

    @Binds
    @SingleIn(AppScope::class)
    fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @SingleIn(AppScope::class)
    fun bindCentralizeAuthRepository(repository: DefaultCentralizeAuthRepository): CentralizeAuthRepository

    @Binds
    @SingleIn(AppScope::class)
    fun bindRemoteAuthDataSource(dataSource: DefaultRemoteAuthDataSource): RemoteAuthDataSource

    @Binds
    @SingleIn(AppScope::class)
    fun bindLocalAuthDataSource(dataSource: DefaultLocalAuthDataSource): LocalAuthDataSource
}