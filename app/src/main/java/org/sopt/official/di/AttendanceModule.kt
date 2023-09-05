package org.sopt.official.di

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
abstract class AttendanceModule {
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