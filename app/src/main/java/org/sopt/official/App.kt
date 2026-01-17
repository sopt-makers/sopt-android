/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.messaging.FirebaseMessaging
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroApplication
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.di.AppGraph
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

class App : Application(), MetroApplication {
    val appGraph by lazy {
        createGraphFactory<AppGraph.Factory>().create(this)
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph

    val navigatorProvider: NavigatorProvider
        get() = appGraph.navigatorProvider

    private val dataStore: SoptDataStore get() = appGraph.dataStore

    private val lifecycleOwner: LifecycleOwner
        get() = ProcessLifecycleOwner.get()

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                runCatching {
                    FirebaseMessaging.getInstance().token.await()
                }.onSuccess {
                    dataStore.pushToken = it
                }.onFailure(Timber::e)
            }
        }
    }
}