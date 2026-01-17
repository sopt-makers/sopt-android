/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import dev.zacsweers.metro.Inject
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.deeplink.AppDeeplinkModule
import org.sopt.official.deeplink.AppDeeplinkModuleRegistry
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModule
import org.sopt.official.feature.fortune.deeplink.FortuneDeeplinkModuleRegistry
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.webview.deeplink.WebDeeplinkModule
import org.sopt.official.webview.deeplink.WebDeeplinkModuleRegistry

@Inject
@DeepLinkHandler(value = [AppDeeplinkModule::class, FortuneDeeplinkModule::class, WebDeeplinkModule::class])
class DeepLinkSchemeActivity(
    private val dataStore: SoptDataStore,
    private val navigator: NavigatorProvider
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (dataStore.accessToken.isEmpty()) {
            startActivity(navigator.getAuthActivityIntent())
            finish()
            return
        }

        DeepLinkDelegate(
            AppDeeplinkModuleRegistry(),
            FortuneDeeplinkModuleRegistry(),
            WebDeeplinkModuleRegistry()
        ).dispatchFrom(this)
        finish()
    }
}