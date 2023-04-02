package org.sopt.official.di.attendance

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.repository.attendance.AttendanceRepositoryImpl
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendanceBindsModule {
    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(attendanceRepositoryImpl: AttendanceRepositoryImpl): AttendanceRepository
}