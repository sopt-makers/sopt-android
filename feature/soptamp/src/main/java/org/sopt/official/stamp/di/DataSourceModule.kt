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
import org.sopt.official.stamp.data.remote.source.RemoteMissionsDataSource
import org.sopt.official.stamp.data.remote.source.RemoteRankingDataSource
import org.sopt.official.stamp.data.remote.source.RemoteUserDataSource
import org.sopt.official.stamp.data.source.MissionsDataSource
import org.sopt.official.stamp.data.source.RankingDataSource
import org.sopt.official.stamp.data.source.UserDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindRemoteMissionsDataSource(
        source: RemoteMissionsDataSource
    ): MissionsDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteUserDataSource(
        source: RemoteUserDataSource
    ): UserDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteRankDataSource(
        source: RemoteRankingDataSource
    ): RankingDataSource
}
