/*
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.stamp.data.repository.RemoteMissionsRepository
import org.sopt.official.stamp.data.repository.RemoteRankingRepository
import org.sopt.official.stamp.data.repository.UserRepositoryImpl
import org.sopt.official.stamp.data.repository.StampRepositoryImpl
import org.sopt.official.stamp.domain.repository.MissionsRepository
import org.sopt.official.stamp.domain.repository.RankingRepository
import org.sopt.official.stamp.domain.repository.StampRepository
import org.sopt.official.stamp.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMissionsRepository(
        repository: RemoteMissionsRepository
    ): MissionsRepository

    @Binds
    @Singleton
    abstract fun bindRankRepository(
        repository: RemoteRankingRepository
    ): RankingRepository

    @Binds
    @Singleton
    abstract fun bindStampRepository(
        repository: StampRepositoryImpl
    ): StampRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        repository: UserRepositoryImpl
    ): UserRepository
}
