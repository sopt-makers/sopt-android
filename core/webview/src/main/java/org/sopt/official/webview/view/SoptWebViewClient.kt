package org.sopt.official.webview.view

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

class SoptWebViewClient(
    private val dataStore: SoptDataStore
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        val script = "window.localStorage.setItem('serviceAccessToken', '${dataStore.playgroundToken}');"
        view?.evaluateJavascript(script) {
            Timber.d("SOPT onPageStarted callback $it")
        }
        super.onPageStarted(view, url, favicon)
    }
}
