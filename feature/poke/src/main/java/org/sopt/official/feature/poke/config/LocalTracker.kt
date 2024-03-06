package org.sopt.official.feature.poke.config

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.sopt.official.analytics.AmplitudeTracker

val LocalTracker = compositionLocalOf<AmplitudeTracker> {
    error("No LocalTrackerProvider provided")
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TrackerEntryPoint {
    fun amplitudeTracker(): AmplitudeTracker
}

@Composable
fun rememberTracker(context: Context) = remember(context) {
    EntryPointAccessors
        .fromApplication<TrackerEntryPoint>(context)
        .amplitudeTracker()
}
