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

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import dagger.hilt.android.EntryPointAccessors
import org.sopt.official.network.BuildConfig
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

    private val dataStore by lazy {
        EntryPointAccessors.fromApplication(
            context,
            SoptWebViewEntryPoint::class.java
        ).dataStore()
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = true
            setSupportZoom(false)
            webViewClient = SoptWebViewClient(dataStore)
            userAgentString = "Chrome/130.0.0.0 Mobile"
        }
        initWebView()
    }

    open fun initWebView() {
        setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }

    protected fun release() {
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
}
