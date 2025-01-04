/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
import dagger.hilt.android.EntryPointAccessors
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.util.extractQueryParameter
import org.sopt.official.common.util.isExpiredDate
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.feature.notification.SchemeActivity.Argument.NotificationInfo
import org.sopt.official.feature.notification.detail.NotificationDetailActivity
import org.sopt.official.network.persistence.SoptDataStoreEntryPoint
import timber.log.Timber
import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class SchemeActivity : AppCompatActivity() {
    private val dataStore by lazy {
        EntryPointAccessors
            .fromApplication<SoptDataStoreEntryPoint>(applicationContext)
            .soptDataStore()
    }
    private val args by serializableExtra(Argument("", ""))

    @Inject
    lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag("gio").e("onCreate invoked...")
        super.onCreate(savedInstanceState)
        trackClickPush()
        handleDeepLink()
    }

    private fun trackClickPush() {
        Timber.tag("gio").e(args?.notificationInfo.toString())
        args?.notificationInfo?.let { notificationInfo: NotificationInfo ->
            tracker.track(
                type = EventType.CLICK, name = "push", properties = mapOf(
                    "notification_id" to notificationInfo.id,
                    "send_timestamp" to notificationInfo.sendAt,
                    "leadtime" to ChronoUnit.DAYS.between(LocalDate.parse(notificationInfo.sendAt), LocalDate.now()),
                    "deeplink_url" to args?.link
                )
            )
        }
    }

    private fun handleDeepLink() {
        val link = args?.link
        val linkIntent = if (link.isNullOrBlank()) {
            NotificationDetailActivity.getIntent(
                this,
                args?.notificationId.orEmpty()
            )
        } else {
            checkLinkExpiration(link)
        }.putExtra(
            NotificationDetailActivity.EXTRA_FROM,
            NotificationDetailActivity.Companion.OpenMethod.PUSH.korName
        )

        when (!isTaskRoot) {
            true -> startActivity(linkIntent)
            false -> TaskStackBuilder.create(this).apply {
                if (!isIntentToHome()) {
                    addNextIntentWithParentStack(
                        DeepLinkType.getHomeIntent(UserStatus.of(dataStore.userStatus))
                    )
                }
                addNextIntent(linkIntent)
            }.startActivities()
        }
        finish()
    }

    private fun checkLinkExpiration(link: String): Intent {
        return try {
            val expiredAt = link.extractQueryParameter("expiredAt")
            when (expiredAt.isExpiredDate()) {
                true -> DeepLinkType.getHomeIntent(
                    UserStatus.of(dataStore.userStatus),
                    DeepLinkType.EXPIRED
                )

                else -> when (link.contains("http://") || link.contains("https://")) {
                    true -> Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    )

                    false -> DeepLinkType.of(link).getIntent(
                        this,
                        UserStatus.of(dataStore.userStatus),
                        link
                    )
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            DeepLinkType.getHomeIntent(
                UserStatus.of(dataStore.userStatus),
                DeepLinkType.UNKNOWN
            )
        }
    }

    private fun isIntentToHome(): Boolean {
        return intent.action == Intent.ACTION_MAIN && (intent.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    /**
     *@param notificationInfo 푸시 알림을 클릭해서 들어온 경우, 푸시 알림에 관한 정보를 제공합니다.
     * 푸시 알림을 통해 들어온 것이 아닐 경우에는 null 입니다.
     * */
    data class Argument(
        val notificationId: String,
        val link: String,
        val notificationInfo: NotificationInfo? = null
    ) : Serializable {
        data class NotificationInfo(
            val id: String,
            val sendAt: String,
            val title: String,
            val content: String,
            val relatedFeature: String,
        ) : Serializable
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: Argument) = Intent(context, SchemeActivity::class.java)
            .putExtra("args", args)
    }
}
