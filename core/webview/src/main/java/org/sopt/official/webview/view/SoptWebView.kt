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
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
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
