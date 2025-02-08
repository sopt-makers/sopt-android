package org.sopt.official.data.schedule.di

import com.sopt.official.domain.schedule.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.schedule.repository.DefaultScheduleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    @Singleton
    fun bindDefaultScheduleRepositoryRepository(defaultScheduleRepository: DefaultScheduleRepository): ScheduleRepository
}
