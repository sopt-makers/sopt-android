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
package org.sopt.official.data.soptamp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.soptamp.BuildConfig
import org.sopt.official.domain.soptamp.constant.Constant
import org.sopt.official.domain.soptamp.constant.Strings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConstantModule {
    @Provides
    @Singleton
    @Strings(Constant.SOPTAMP_DATA_STORE)
    fun provideSoptampDataStoreKey(): String = BuildConfig.SOPTAMP_DATA_STORE_KEY
}