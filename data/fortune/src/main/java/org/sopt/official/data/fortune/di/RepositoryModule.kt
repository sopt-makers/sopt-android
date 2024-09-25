package org.sopt.official.data.fortune.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.fortune.remote.api.FortuneApi
import org.sopt.official.data.fortune.repository.DefaultFortuneRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDefaultFortuneRepository(fortuneApi: FortuneApi): DefaultFortuneRepository = DefaultFortuneRepository(fortuneApi)
}
