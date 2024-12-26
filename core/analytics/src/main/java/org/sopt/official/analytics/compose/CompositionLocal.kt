package org.sopt.official.analytics.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.sopt.official.analytics.Tracker

@Composable
fun ProvideTracker(
    tracker: Tracker,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalTracker provides tracker
    ) {
        content()
    }
}
