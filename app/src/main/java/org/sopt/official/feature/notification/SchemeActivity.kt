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
package org.sopt.official.feature.notification

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.util.extractQueryParameter
import org.sopt.official.common.util.isExpiredDate
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.feature.notification.detail.NotificationDetailActivity
import org.sopt.official.localstorage.di.StorageEntryPoint
import org.sopt.official.model.UserStatus
import org.sopt.official.model.toViewType
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject
import androidx.core.net.toUri

@AndroidEntryPoint
class SchemeActivity : AppCompatActivity() {
    private val userStorage by lazy {
        EntryPointAccessors
            .fromApplication<StorageEntryPoint>(applicationContext)
            .userStorage()
    }

    private val args by serializableExtra(Argument("", ""))
    private val isPush by lazy { intent.getBooleanExtra(EXTRA_IS_PUSH, false) }

    @Inject
    lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launchType = when (isTaskRoot) {
            true -> NotificationLaunchType.COLD_START
            false -> NotificationLaunchType.WARM_START
        }
        lifecycleScope.launch {
            val userStatus = suspendRunCatching {
                userStorage.userStatus.first()
            }.onFailure { exception ->
                Timber.e(exception, "사용자 상태 조회 실패")
            }.getOrDefault(UserStatus.UNAUTHENTICATED)

            if (isPush) {
                suspendRunCatching {
                    tracker.trackViewType(
                        event = NotificationAnalyticsEvent.CLICK_PUSH,
                        viewType = userStatus.toViewType(),
                        properties = buildMap {
                            args?.notificationId
                                ?.takeIf(String::isNotBlank)
                                ?.let { put(NotificationAnalyticsPropertyKey.NOTIFICATION_ID, it) }
                            put(
                                NotificationAnalyticsPropertyKey.NOTIFICATION_LINK_TYPE,
                                args?.link.toNotificationLinkType().value,
                            )
                            put(
                                NotificationAnalyticsPropertyKey.NOTIFICATION_LAUNCH_TYPE,
                                launchType.value,
                            )
                        },
                    )
                }.onFailure { exception ->
                    Timber.e(exception, "click_push 전송 실패")
                }
            }
            handleDeepLink(userStatus)
        }
    }

    private fun handleDeepLink(userStatus: UserStatus) {
        val link = args?.link
        val linkIntent = if (link.isNullOrBlank()) {
            NotificationDetailActivity.getIntent(
                this,
                args?.notificationId.orEmpty(),
                userStatus,
            )
        } else {
            checkLinkExpiration(link, userStatus)
        }

        when (!isTaskRoot) {
            true -> startActivity(linkIntent)
            false -> TaskStackBuilder.create(this).apply {
                if (!isIntentToHome()) {
                    addNextIntentWithParentStack(
                        DeepLinkType.getIntent(userStatus)
                    )
                }
                addNextIntent(linkIntent)
            }.startActivities()
        }
        finish()
    }

    private fun checkLinkExpiration(
        link: String,
        userStatus: UserStatus,
    ): Intent {
        return try {
            val expiredAt = link.extractQueryParameter("expiredAt")
            when (expiredAt.isExpiredDate()) {
                true -> DeepLinkType.getIntent(
                    userStatus,
                    DeepLinkType.EXPIRED
                )

                else -> when (link.startsWith("http://") || link.startsWith("https://")) {
                    true -> Intent(
                        Intent.ACTION_VIEW,
                        link.toUri()
                    )

                    false -> DeepLinkType.of(link).getIntent(
                        this,
                        userStatus,
                        link
                    )
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            DeepLinkType.getIntent(
                userStatus,
                DeepLinkType.UNKNOWN
            )
        }
    }

    private fun isIntentToHome(): Boolean {
        return intent.action == Intent.ACTION_MAIN && (intent.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    data class Argument(
        val notificationId: String,
        val link: String,
    ) : Serializable

    companion object {
        private const val EXTRA_IS_PUSH = "isPush"

        @JvmStatic
        fun getIntent(
            context: Context,
            args: Argument,
            isPush: Boolean = false,
        ) = Intent(context, SchemeActivity::class.java).apply {
            putExtra("args", args)
            putExtra(EXTRA_IS_PUSH, isPush)
        }
    }
}
