package org.sopt.official.analytics.compose

import androidx.compose.runtime.staticCompositionLocalOf
import org.sopt.official.analytics.Tracker

val LocalTracker = staticCompositionLocalOf<Tracker> {
    error("No Tracker provided")
}
