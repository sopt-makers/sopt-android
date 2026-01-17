/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.provider.Browser
import androidx.browser.customtabs.CustomTabsIntent

object AuthIntentFactory {
    fun authIntentWithAuthTab(context: Context, uri: Uri, state: String, resultReceiver: ResultReceiver): Intent {
        val bundle = Bundle().apply {
            putParcelable(Constants.AUTH_URI_KEY, uri)
            putString(Constants.STATE, state)
            putParcelable(Constants.RESULT_RECEIVER, resultReceiver)
        }

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.intent.putExtra(
            Browser.EXTRA_HEADERS,
            Bundle().apply {
                putString(Constants.USER_AGENT, Constants.CHROME_USER_AGENT)
            }
        )

        customTabsIntent.intent.data = uri

        return Intent(context, AuthCodeHandlerActivity::class.java)
            .putExtra(Constants.AUTH_INTENT_BUNDLE_KEY, bundle)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
