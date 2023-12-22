package org.sopt.official.data.poke.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.poke.repository_impl.PokeRepositoryImpl
import org.sopt.official.domain.poke.repository.PokeRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPokeRepository(
        impl: PokeRepositoryImpl,
    ): PokeRepository
}