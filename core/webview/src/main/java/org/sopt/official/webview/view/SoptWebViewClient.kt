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
package org.sopt.official.webview.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

class SoptWebViewClient(
    private val dataStore: SoptDataStore,
    private val context: Context
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url?.scheme == "intent") {
            try {
                val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)

                val isInstalled = try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.packageManager.getPackageInfo(
                            "com.kakao.talk",
                            PackageManager.PackageInfoFlags.of(0)
                        )
                    } else {
                        context.packageManager.getPackageInfo("com.kakao.talk", 0)
                    }
                    true
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }

                if (isInstalled) {
                    context.startActivity(intent)
                } else {
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.kakao.talk")).apply {
                        context.startActivity(this)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.message)
            }
            return true
        } else return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        val script = """
            window.localStorage.setItem('serviceAccessToken', '${dataStore.playgroundToken}');
        """.trimIndent()
        view?.evaluateJavascript(script) {}
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        (view?.parent as? SwipeRefreshLayout)?.isRefreshing = false
    }
}
