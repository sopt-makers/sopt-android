package org.sopt.official.security.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.security.CryptoManager
import org.sopt.official.security.CryptoManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {
    @Singleton
    @Binds
    abstract fun bindsCryptoManager(cryptoManagerImpl: CryptoManagerImpl): CryptoManager
}