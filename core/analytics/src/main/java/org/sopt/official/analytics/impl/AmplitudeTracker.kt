/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.analytics.impl

import android.app.Application
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify
import dev.zacsweers.metro.Inject
import org.sopt.official.analytics.BuildConfig
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import timber.log.Timber

class AmplitudeTracker @Inject constructor(
    private val application: Application
) : Tracker {
    private val amplitude = Amplitude(
        Configuration(
            apiKey = if (BuildConfig.DEBUG) {
                BuildConfig.DEV_AMPLITUDE_KEY
            } else {
                BuildConfig.AMPLITUDE_KEY
            },
            context = application.applicationContext
        )
    )

    override fun track(type: EventType, name: String, properties: Map<String, Any?>) {
        if (BuildConfig.DEBUG) {
            Timber.tag("Ample").d("Amplitude: ${type.prefix}_$name properties: $properties")
        }
        amplitude.track(eventType = "${type.prefix}_$name", eventProperties = properties)
    }

    override fun setNotificationStateToUserProperties(value: Boolean) {
        val identify = Identify()
        identify.setOnce("state_of_push_notification", value)
        amplitude.identify(identify)
    }
}