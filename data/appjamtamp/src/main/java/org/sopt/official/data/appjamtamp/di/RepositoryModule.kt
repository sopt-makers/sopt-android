package org.sopt.official.data.appjamtamp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.appjamtamp.repository.AppjamtampRepositoryImpl
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAppjamtampRepository(
        appjamtampRepositoryImpl: AppjamtampRepositoryImpl
    ): AppjamtampRepository

}