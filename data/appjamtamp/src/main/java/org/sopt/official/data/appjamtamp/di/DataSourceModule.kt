package org.sopt.official.data.appjamtamp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.datasourceimpl.AppjamtampDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAppjamtampDataSource(appjamtampDataSourceImpl: AppjamtampDataSourceImpl): AppjamtampDataSource
}