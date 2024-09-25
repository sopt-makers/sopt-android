package org.sopt.official.data.fortune.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.fortune.repository.DefaultFortuneRepository
import org.sopt.official.domain.fortune.repository.FortuneRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDefaultFortuneRepository(defaultFortuneRepository: DefaultFortuneRepository): FortuneRepository
}
