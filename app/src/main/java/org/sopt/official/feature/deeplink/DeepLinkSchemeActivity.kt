/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.deeplink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.deeplink.AppDeeplinkModule
import org.sopt.official.deeplink.AppDeeplinkModuleRegistry
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.webview.deeplink.WebDeeplinkModule
import org.sopt.official.webview.deeplink.WebDeeplinkModuleRegistry
import org.sopt.official.webview.view.WebViewActivity
import org.sopt.official.webview.view.WebViewActivity.Companion.INTENT_URL
import javax.inject.Inject

@AndroidEntryPoint
@DeepLinkHandler(value = [AppDeeplinkModule::class, WebDeeplinkModule::class])
class DeepLinkSchemeActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenStorage: TokenStorage

    @Inject
    lateinit var navigator: NavigatorProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val accessToken = tokenStorage.accessToken.first()

            if (accessToken.isEmpty()) {
                startActivity(navigator.getAuthActivityIntent())
                finish()
                return@launch
            }

            if (intent?.data?.scheme == "https") {
                Intent(this@DeepLinkSchemeActivity, WebViewActivity::class.java).apply {
                    putExtra(INTENT_URL, intent?.dataString)
                }.also {
                    startActivity(it)
                }
                finish()
                return@launch
            }

            dispatchDeepLink()
        }
    }

    private fun dispatchDeepLink() {
        val deepLinkDelegate = DeepLinkDelegate(
            AppDeeplinkModuleRegistry(),
            WebDeeplinkModuleRegistry()
        )
        deepLinkDelegate.dispatchFrom(this)
        finish()
    }
}
