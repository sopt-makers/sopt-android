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

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.graphics.toColorInt
import dagger.hilt.android.EntryPointAccessors
import org.sopt.official.webview.BuildConfig
import org.sopt.official.webview.di.SoptWebViewEntryPoint

@SuppressLint("SetJavaScriptEnabled")
open class SoptWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    private val tokenStorage by lazy {
        EntryPointAccessors.fromApplication(
            context,
            SoptWebViewEntryPoint::class.java
        ).tokenStorage()
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setBackgroundColor(BACKGROUND_COLOR)
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            useWideViewPort = true
            loadWithOverviewMode = true
            offscreenPreRaster = true
            builtInZoomControls = false
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = true
            setSupportZoom(false)
            webViewClient = SoptWebViewClient(tokenStorage)
            userAgentString = "Chrome/130.0.0.0 Mobile"
        }
        initWebView()
    }

    open fun initWebView() {
        if (isWebContentsDebuggingInitialized) return
        setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        isWebContentsDebuggingInitialized = true
    }

    internal fun release() {
        stopLoading()
        loadUrl(Uri.EMPTY.toString())
        webChromeClient = null
        settings.javaScriptEnabled = false
        settings.blockNetworkImage = false
        clearHistory()
        clearCache(true)
        removeAllViews()
        destroy()
    }

    companion object {
        // WebView 기본 배경은 테마와 무관하게 흰색이라 로딩 전 흰 화면이 노출되므로 서비스 배경색(black_100)으로 고정
        private val BACKGROUND_COLOR = "#0F1010".toColorInt()

        // setWebContentsDebuggingEnabled는 프로세스 전역 설정이라 인스턴스마다 다시 호출할 필요가 없음
        private var isWebContentsDebuggingInitialized = false
    }
}
