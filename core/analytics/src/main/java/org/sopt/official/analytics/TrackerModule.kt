package org.sopt.official.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.analytics.impl.AmplitudeTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TrackerModule {
    @Binds
    @Singleton
    fun bindAmplitudeTracker(amplitudeTracker: AmplitudeTracker): Tracker
}
