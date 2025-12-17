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
        return if (super.shouldOverrideUrlLoading(view, request)) {
            true
        } else {
            handleMarketScheme(view, url)
                || handleKakaoLinkScheme(view, url)
                || handleTelScheme(view, url)
                || handleMailScheme(view, url)
                || handleIntentScheme(view, url)
                || handleNotionScheme(view, url)
        }
    }

    /**
     * Comment by HyunWoo Lee
     * 저사양기기/낮은 API 기기들은 위의 함수가 아닌 해당 함수를 실행할 수 있어
     * 추가 대응을 위해 구현을 해놓습니다.
     */
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Timber.d("SoptWebViewClient#shouldOverrideUrlLoading url: $url")
        return if (super.shouldOverrideUrlLoading(view, url)) {
            true
        } else {
            val uri = url?.toUri() ?: return true
            handleMarketScheme(view, uri)
                || handleKakaoLinkScheme(view, uri)
                || handleTelScheme(view, uri)
                || handleMailScheme(view, uri)
                || handleIntentScheme(view, uri)
                || handleNotionScheme(view, uri)
        }
    }

    private fun handleMailScheme(view: WebView?, url: Uri): Boolean {
        if (url.scheme == "mailto") {
            view?.context?.navigateTo(url)
            return true
        }
        return false
    }

    private fun handleIntentScheme(view: WebView?, url: Uri): Boolean {
        try {
            if (url.scheme == "intent") {
                val urlString = url.toString()
                val intent = Intent.parseUri(urlString, Intent.URI_INTENT_SCHEME).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    component = null
                    selector = null
                }
                try {
                    view?.context?.startActivity(intent)
                    return true
                } catch (e: ActivityNotFoundException) {
                    val packageName = intent.`package`
                    if (packageName != null) {
                        view?.context?.navigateTo("market://details?id=${packageName}".toUri())
                        return true
                    }
                }
                return true
            }
        } catch (e: Exception) {
            Timber.e("SoptWebViewClient#handleIntentScheme failed $e")
        }
        return false
    }

    private fun handleTelScheme(view: WebView?, url: Uri): Boolean {
        if (url.scheme?.startsWith("tel") == true) {
            view?.context?.navigateTo(url)
            return true
        }
        return false
    }

    private fun handleKakaoLinkScheme(view: WebView?, url: Uri): Boolean {
        if (url.scheme?.startsWith("intent:kakaolink://") == true) {
            val kakaoLinkScheme = url.toString().replace("intent:", "").toUri()
            view?.context?.navigateTo(kakaoLinkScheme)
            return true
        }
        return false
    }

    private fun handleMarketScheme(view: WebView?, url: Uri): Boolean {
        if (url.scheme == "market") {
            view?.context?.navigateTo(url)
            return true
        }
        return false
    }

    private fun handleNotionScheme(view: WebView?, url: Uri?): Boolean {
        if (url?.scheme?.startsWith("notion") == true) {
            Intent(Intent.ACTION_VIEW, url).apply {
                view?.context?.startActivity(this)
            }
            return true
        }
        return false
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
