package org.sopt.official.data.soptlog.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.soptlog.repository.DefaultSoptLogRepository
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindDefaultSoptLogRepository(defaultSoptLogRepository: DefaultSoptLogRepository): SoptLogRepository
}
