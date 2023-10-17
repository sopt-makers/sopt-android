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
package org.sopt.official.di.attendance

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.OperationRetrofit
import org.sopt.official.data.repository.attendance.AttendanceRepositoryImpl
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendanceBindsModule {
    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(attendanceRepositoryImpl: AttendanceRepositoryImpl): AttendanceRepository

    companion object {
        @Provides
        @Singleton
        fun provideAttendanceService(@OperationRetrofit retrofit: Retrofit): AttendanceService =
            retrofit.create(AttendanceService::class.java)
    }
}
