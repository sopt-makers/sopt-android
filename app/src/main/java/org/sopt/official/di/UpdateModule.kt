package org.sopt.official.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import org.sopt.official.feature.update.InAppUpdateManager
import org.sopt.official.feature.update.InAppUpdateManagerImpl

@Module
@InstallIn(ActivityComponent::class)
object UpdateModule {
    @Provides
    @ActivityScoped
    fun provideInAppUpdateManager(
        updateManager: InAppUpdateManagerImpl
    ): InAppUpdateManager = updateManager

    @Provides
    @ActivityScoped
    fun provideAppUpdateManager(
        @ActivityContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)
}
