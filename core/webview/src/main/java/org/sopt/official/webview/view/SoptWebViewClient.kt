/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.webview.view

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

class SoptWebViewClient(
    private val dataStore: SoptDataStore
) : WebViewClient() {
    private var hasReloaded = false

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (hasReloaded) return

        val script = """
            (function() {
                return new Promise((resolve, reject) => {
                    window.localStorage.setItem('serviceAccessToken', '${dataStore.playgroundToken}');
                    resolve();
                });
            })().then(() => {
                window.location.reload();
                return window.localStorage.getItem('serviceAccessToken');
            });
        """.trimIndent()

        view?.evaluateJavascript(script) { localStorageToken ->
            Timber.tag("SoptWebViewClient").d("localStorageToken: $localStorageToken")
            Timber.tag("SoptWebViewClient").d("playgroundToken: $dataStore.playgroundToken")
            if (localStorageToken == dataStore.playgroundToken) hasReloaded = true
        }

    }
}
