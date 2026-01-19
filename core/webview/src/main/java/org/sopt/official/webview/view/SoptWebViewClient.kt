/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

class SoptWebViewClient(
    private val dataStore: SoptDataStore
) : WebViewClient() {
    val cookieManager: CookieManager = CookieManager.getInstance()

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString().toUri()

        Timber.d("SoptWebViewClient#shouldOverrideUrlLoading url: $url")

        if (super.shouldOverrideUrlLoading(view, request)) return true

        return when {
            url.toString().startsWith("intent:kakaolink://") -> handleKakaoLinkScheme(view, url)
            url.scheme?.startsWith("tel") == true -> navigateToExternal(view, url)

            url.scheme == "mailto" -> navigateToExternal(view, url)
            url.scheme == "market" -> navigateToExternal(view, url)
            url.scheme == "notion" -> handleNotionScheme(view, url)
            url.scheme == "nmap" -> handleNMapScheme(view, url)
            url.scheme == "intent" -> handleIntentScheme(view, url)

            else -> false
        }
    }

    private fun navigateToExternal(view: WebView?, url: Uri): Boolean {
        view?.context?.navigateTo(url)
        return true
    }

    private fun handleKakaoLinkScheme(view: WebView?, url: Uri): Boolean {
        val kakaoLinkScheme = url.toString().replace("intent:", "").toUri()
        view?.context?.navigateTo(kakaoLinkScheme)
        return true
    }

    private fun handleNotionScheme(view: WebView?, url: Uri): Boolean {
        try {
            Intent(Intent.ACTION_VIEW, url).apply {
                view?.context?.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            val webUrl = url.toString()
                .replaceFirst("notion://", "https://")

            view?.context?.navigateTo(webUrl.toUri())
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }

        return true
    }

    private fun handleNMapScheme(view: WebView?, url: Uri): Boolean {
        try {
            Intent(Intent.ACTION_VIEW, url).apply {
                view?.context?.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            view?.context?.navigateTo("market://details?id=com.nhn.android.nmap".toUri())
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }

        return true
    }

    private fun handleIntentScheme(view: WebView?, url: Uri): Boolean {
        val intent = runCatching {
            val urlString = url.toString()
            Intent.parseUri(urlString, Intent.URI_INTENT_SCHEME).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                component = null
                selector = null
            }
        }.getOrElse { e ->
            Timber.e("SoptWebViewClient#handleIntentScheme failed $e")
            return true
        }

        try {
            view?.context?.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val packageName = intent.`package`
            if (packageName != null) {
                view?.context?.navigateTo("market://details?id=${packageName}".toUri())
            }
        }
        return true
    }

    private fun Context.navigateTo(to: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, to)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Timber.e("Uri is not valid. uri: $to")
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        cookieManager.setCookie(COOKIE_DOMAIN, "Refresh-Token=${dataStore.refreshToken}") {
            cookieManager.flush()
        }

        val script = """
            window.localStorage.setItem('serviceAccessToken', '${dataStore.accessToken}');
        """.trimIndent()
        view?.evaluateJavascript(script) {}
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        (view?.parent as? SwipeRefreshLayout)?.isRefreshing = false
    }

    companion object {
        private const val COOKIE_DOMAIN = ".sopt.org"
    }
}
