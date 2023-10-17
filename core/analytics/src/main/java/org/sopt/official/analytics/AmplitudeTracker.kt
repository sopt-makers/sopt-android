/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
