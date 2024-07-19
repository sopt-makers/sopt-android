/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.soptamp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.soptamp.remote.api.RankService
import org.sopt.official.data.soptamp.remote.api.S3Service
import org.sopt.official.data.soptamp.remote.api.SoptampService
import org.sopt.official.data.soptamp.remote.api.StampService
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {
    @Provides
    @Singleton
    fun provideStampService(@AppRetrofit retrofit: Retrofit): StampService = retrofit.create(StampService::class.java)

    @Provides
    @Singleton
    fun provideSoptampService(@AppRetrofit retrofit: Retrofit): SoptampService = retrofit.create(SoptampService::class.java)

    @Provides
    @Singleton
    fun provideRankingService(@AppRetrofit retrofit: Retrofit): RankService = retrofit.create(RankService::class.java)

    @Provides
    @Singleton
    fun provideS3Service(@AppRetrofit(false) retrofit: Retrofit): S3Service = retrofit.create(S3Service::class.java)
}
