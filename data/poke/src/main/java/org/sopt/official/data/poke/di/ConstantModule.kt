package org.sopt.official.data.poke.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.poke.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConstantModule {
    @Provides
    @Singleton
    @Strings(Constant.POKE_DATA_STORE)
    fun providePokeDataStoreKey(): String = BuildConfig.POKE_DATA_STORE_KEY
}