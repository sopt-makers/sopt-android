package org.sopt.official.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.feature.navigator.NavigatorProviderIntent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    @Singleton
    fun bindNavigatorIntent(navigatorProviderIntent: NavigatorProviderIntent): NavigatorProvider
}
