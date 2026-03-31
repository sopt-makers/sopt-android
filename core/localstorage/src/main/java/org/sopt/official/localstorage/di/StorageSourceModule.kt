package org.sopt.official.localstorage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.localstorage.source.GlobalStorage
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.localstorage.sourceimpl.DefaultSoptStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageSourceModule {
    @Binds
    @Singleton
    fun bindTokenStorage(
        impl: DefaultSoptStorage
    ): TokenStorage

    @Binds
    @Singleton
    fun bindUserStorage(
        impl: DefaultSoptStorage
    ): UserStorage

    @Binds
    @Singleton
    fun bindGlobalStorage(
        impl: DefaultSoptStorage
    ): GlobalStorage

}