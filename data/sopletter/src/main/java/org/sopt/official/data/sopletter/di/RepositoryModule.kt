package org.sopt.official.data.sopletter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.sopletter.repository.SopletterRepositoryImpl
import org.sopt.official.sopletter.repository.SopletterWriteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSopletterRepository(
        impl: SopletterRepositoryImpl
    ): SopletterWriteRepository
}