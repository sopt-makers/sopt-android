package org.sopt.official.data.sopletter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.datasourceimpl.SopletterDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSopletterDataSource(
        impl: SopletterDataSourceImpl
    ): SopletterDataSource
}