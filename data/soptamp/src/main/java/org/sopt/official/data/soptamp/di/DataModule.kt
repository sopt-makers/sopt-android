/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.sopt.official.data.soptamp.repository.ImageUploaderRepositoryImpl
import org.sopt.official.data.soptamp.repository.RemoteMissionsRepository
import org.sopt.official.data.soptamp.repository.RemoteRankingRepository
import org.sopt.official.data.soptamp.repository.StampRepositoryImpl
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository
import org.sopt.official.domain.soptamp.repository.MissionsRepository
import org.sopt.official.domain.soptamp.repository.RankingRepository
import org.sopt.official.domain.soptamp.repository.StampRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMissionsRepository(repository: RemoteMissionsRepository): MissionsRepository

    @Binds
    @Singleton
    abstract fun bindRankRepository(repository: RemoteRankingRepository): RankingRepository

    @Binds
    @Singleton
    abstract fun bindStampRepository(repository: StampRepositoryImpl): StampRepository

    @Binds
    @Singleton
    abstract fun bindImageUploaderRepository(repository: ImageUploaderRepositoryImpl): ImageUploaderRepository
}
