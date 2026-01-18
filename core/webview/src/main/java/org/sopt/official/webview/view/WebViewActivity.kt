/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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

import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.airbnb.deeplinkdispatch.DeepLink
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import java.net.URLDecoder
import mozilla.components.support.utils.DownloadUtils
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.webview.databinding.ActivityWebViewBinding

@DeepLink("sopt://web")
@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(WebViewActivity::class)
class WebViewActivity @Inject constructor() : AppCompatActivity() {
    private val binding by viewBinding(ActivityWebViewBinding::inflate)
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val imageResult = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri == null) {
            filePathCallback?.onReceiveValue(null)
            filePathCallback = null
        } else {
            val data = Intent().apply { data = uri }
            filePathCallback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(Activity.RESULT_OK, data))
            filePathCallback = null
        }
    }
    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            toast("권한을 받아오지 못했습니다.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (filePathCallback != null) {
                    this@WebViewActivity.filePathCallback = filePathCallback
                }

                imageResult.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                return true
            }
        }
        binding.webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, _ ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                val fileName = DownloadUtils.guessFileName(
                    contentDisposition = URLDecoder.decode(contentDisposition, "utf-8"),
                    url = url,
                    mimeType = mimetype
                )
                val downloadManager = getSystemService<DownloadManager>() ?: return@setDownloadListener
                val request = DownloadManager.Request(Uri.parse(url))
                    .setMimeType(mimetype)
                    .addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url))
                    .addRequestHeader("User-Agent", userAgent)
                    .setTitle(fileName)
                    .apply {
                        allowScanningByMediaScanner()
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            fileName
                        )
                    }
                downloadManager.enqueue(request)
            } else {
                permissionRequestLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                if (message?.contains("android: false") == true) {
                    result?.confirm()
                    return true
                }

                return super.onJsAlert(view, url, message, result)
            }
        }

        handleLinkUrl()
        handleOnBackPressed()
    }

    private fun handleLinkUrl() {
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            val url = intent.extras?.getString(INTENT_URL) ?: intent.getStringExtra(INTENT_URL) ?: "https://google.com"
            binding.webView.loadUrl(url)
        } else {
            val linkUrl = intent.getStringExtra(INTENT_URL)
            linkUrl?.let { binding.webView.loadUrl(it) }
        }
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(owner = this) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                if (!isFinishing) finish()
            }
        }
    }

    companion object {
        const val INTENT_URL = "url"
    }
}
