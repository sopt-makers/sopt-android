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
package org.sopt.official.feature.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.io.Serializable
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.model.UserStatus
import org.sopt.official.designsystem.SoptTheme as SoptAppTheme

private val applicationNavigator by lazy {
    EntryPointAccessors.fromApplication(
        appContext,
        NavigatorEntryPoint::class.java
    ).navigatorProvider()
}


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var userStorage: UserStorage

    private var intentState by mutableStateOf<Intent?>(null)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentState = intent
        val startArgs = intent.getSerializableExtra(ARGS) as? StartArgs
        val userStatus = startArgs?.userStatus ?: UserStatus.UNAUTHENTICATED

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.TRANSPARENT,
            ),
        )
        setContent {
            SoptAppTheme {
                SoptTheme {
                    ProvideTracker(tracker) {
                        MainScreen(
                            userStatus = userStatus,
                            intentState = intentState,
                            applicationNavigator = applicationNavigator
                        )
                    }
                }
            }
        }
        requestNotificationPermissionOnce(userStatus)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        intentState = intent
    }

    private fun requestNotificationPermissionOnce(userStatus: UserStatus) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (userStatus == UserStatus.UNAUTHENTICATED) return

        lifecycleScope.launch {
            if (
                ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                userStorage.saveNotificationPermissionRequested(true)
                return@launch
            }

            if (userStorage.isNotificationPermissionRequested.first()) return@launch

            userStorage.saveNotificationPermissionRequested(true)
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    data class StartArgs(
        val userStatus: UserStatus,
        val deepLinkType: DeepLinkType? = null,
    ) : Serializable

    companion object {
        private const val ARGS = "args"

        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, MainActivity::class.java).apply {
            putExtra(ARGS, args)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
