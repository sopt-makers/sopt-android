package org.sopt.official.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.common.navigator.NavigatorProvides
import org.sopt.official.feature.navigator.NavigatorProvidesIntent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {
    @Binds
    @Singleton
    fun bindIntent(navigatorProvidesIntent: NavigatorProvidesIntent): NavigatorProvides
}