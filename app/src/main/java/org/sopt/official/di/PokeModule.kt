package org.sopt.official.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.repository.poke.PokeRepositoryImpl
import org.sopt.official.data.service.poke.PokeService
import org.sopt.official.domain.repository.poke.PokeRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PokeModule {
    @Provides
    @Singleton
    fun providePokeMainService(
        @AppRetrofit retrofit: Retrofit
    ): PokeService {
        return retrofit.create(PokeService::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun bindPokeRepository(repository: PokeRepositoryImpl): PokeRepository
    }
}