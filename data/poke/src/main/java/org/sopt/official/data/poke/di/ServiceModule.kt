package org.sopt.official.data.poke.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.data.poke.service.PokeService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun providePokeService(
        @AppRetrofit(true) retrofit: Retrofit,
    ): PokeService = retrofit.create(PokeService::class.java)
}
