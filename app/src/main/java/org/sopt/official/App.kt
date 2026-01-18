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
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.NavigatorGraph
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.di.AppGraph
import org.sopt.official.feature.auth.utils.di.GoogleGraph
import org.sopt.official.feature.fortune.di.FortuneGraph
import org.sopt.official.feature.mypage.di.AuthGraph
import org.sopt.official.feature.notification.di.NotificationGraph
import org.sopt.official.feature.poke.di.PokeGraph
import org.sopt.official.feature.schedule.di.ScheduleGraph
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.stamp.di.SoptampGraph
import org.sopt.official.webview.di.WebViewGraph
import timber.log.Timber

class App : Application(), MetroApplication,
    MetroAppComponentProviders, ViewModelGraph,
    NavigatorGraph, AuthGraph, WebViewGraph, PokeGraph, GoogleGraph, NotificationGraph,
    SoptampGraph, FortuneGraph, ScheduleGraph {
    val appGraph by lazy {
        createGraphFactory<AppGraph.Factory>().create(this)
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph

    // Delegate implementations to appGraph
    override val dataStore: SoptDataStore get() = appGraph.dataStore
    override val navigatorProvider: NavigatorProvider get() = appGraph.navigatorProvider

    // AuthGraph delegation
    override val authRepository get() = appGraph.authRepository
    override val userRepository get() = appGraph.userRepository
    override val stampRepository get() = appGraph.stampRepository
    override val notificationRepository get() = appGraph.notificationRepository
    override fun myPageActivity() = appGraph.myPageActivity()
    override fun signOutActivity() = appGraph.signOutActivity()
    override fun adjustSentenceActivity() = appGraph.adjustSentenceActivity()

    // WebViewGraph delegation
    override fun webViewActivity() = appGraph.webViewActivity()

    // PokeGraph delegation
    override fun inject(fragment: org.sopt.official.feature.poke.message.MessageListBottomSheetFragment) = appGraph.inject(fragment)
    override fun pokeMainActivity() = appGraph.pokeMainActivity()
    override fun pokeNotificationActivity() = appGraph.pokeNotificationActivity()
    override fun friendListSummaryActivity() = appGraph.friendListSummaryActivity()
    override fun onboardingActivity() = appGraph.onboardingActivity()

    // GoogleGraph delegation
    override val googleLoginManager get() = appGraph.googleLoginManager

    // NotificationGraph delegation
    override fun notificationActivity() = appGraph.notificationActivity()
    override fun notificationDetailActivity() = appGraph.notificationDetailActivity()

    // SoptampGraph delegation
    override fun soptampActivity() = appGraph.soptampActivity()

    // FortuneGraph delegation
    override fun fortuneActivity() = appGraph.fortuneActivity()

    // ScheduleGraph delegation
    override fun scheduleActivity() = appGraph.scheduleActivity()

    // MetroAppComponentProviders delegation
    override val activityProviders get() = appGraph.activityProviders
    override val providerProviders get() = appGraph.providerProviders
    override val receiverProviders get() = appGraph.receiverProviders
    override val serviceProviders get() = appGraph.serviceProviders

    // ViewModelGraph delegation
    override val viewModelProviders get() = appGraph.viewModelProviders
    override val assistedFactoryProviders get() = appGraph.assistedFactoryProviders
    override val manualAssistedFactoryProviders get() = appGraph.manualAssistedFactoryProviders
    override val metroViewModelFactory: MetroViewModelFactory get() = appGraph.metroViewModelFactory

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
