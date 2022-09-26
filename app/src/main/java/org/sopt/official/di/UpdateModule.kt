package org.sopt.official.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object UpdateModule {
    @Provides
    @Singleton
    fun provideAppUpdateManager(
        @ActivityContext context: Context
    ) = AppUpdateManagerFactory.create(context)
}
