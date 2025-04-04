package org.sopt.official.data.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.auth.repository.DefaultAuthRepository
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindDefaultAuthRepository(defaultAuthRepository: DefaultAuthRepository): AuthRepository
}