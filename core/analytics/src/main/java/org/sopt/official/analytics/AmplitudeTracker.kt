package org.sopt.official.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AmplitudeTracker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val amplitude = Amplitude(
        Configuration(
            apiKey = if (BuildConfig.DEBUG) {
                BuildConfig.DEV_AMPLITUDE_KEY
            } else {
                BuildConfig.AMPLITUDE_KEY
            },
            context = context
        )
    )

    fun track(
        type: EventType,
        name: String,
        properties: Map<String, String> = emptyMap()
    ) {
        amplitude.track(eventType = "${type.prefix}_$name", eventProperties = properties)
    }
}
