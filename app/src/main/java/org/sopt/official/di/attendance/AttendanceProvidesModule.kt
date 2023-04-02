package org.sopt.official.di.attendance

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.data.service.attendance.MockAttendanceService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AttendanceProvidesModule {
    @Provides
    @Singleton
    fun provideAttendanceService(): AttendanceService = MockAttendanceService()
}