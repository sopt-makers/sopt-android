package org.sopt.official.data.sopletter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.sopt.official.data.sopletter.repository.SopletterRepositoryImpl
import org.sopt.official.domain.sopletter.repository.SopletterRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSopletterRepository(
        sopletterRepositoryImpl: SopletterRepositoryImpl,
    ): SopletterRepository
}
