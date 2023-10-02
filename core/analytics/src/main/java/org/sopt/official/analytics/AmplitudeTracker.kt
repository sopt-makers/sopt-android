package org.sopt.official.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
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
        properties: Map<String, Any?> = emptyMap()
    ) {
        if (BuildConfig.DEBUG) {
            Timber.d("Amplitude: ${type.prefix}_$name properties: $properties")
        }
        amplitude.track(eventType = "${type.prefix}_$name", eventProperties = properties)
    }
}
