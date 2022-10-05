package org.sopt.official.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import org.sopt.official.feature.update.InAppUpdateManager
import org.sopt.official.feature.update.InAppUpdateManagerImpl

@Module
@InstallIn(ActivityComponent::class)
object UpdateModule {
    @Provides
    @ActivityScoped
    fun provideAppUpdateManager(
        updateManager: InAppUpdateManagerImpl
    ): InAppUpdateManager = updateManager
}
