package org.sopt.official.data.sopletter.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.data.sopletter.onboarding.datasource.SopletterOnboardingDataSource
import org.sopt.official.data.sopletter.onboarding.datasourceimpl.SopletterOnboardingDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SopletterOnboardingDataSourceModule {
    @Binds
    @Singleton
    fun bindSopletterOnboardingDataSource(
        impl: SopletterOnboardingDataSourceImpl
    ) : SopletterOnboardingDataSource
}