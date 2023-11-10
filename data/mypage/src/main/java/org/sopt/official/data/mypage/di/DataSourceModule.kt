package org.sopt.official.data.mypage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.mypage.remote.RemoteUserDataSource
import org.sopt.official.data.mypage.source.UserDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindRemoteUserDataSource(
        source: RemoteUserDataSource
    ): UserDataSource
}
