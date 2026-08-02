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
package org.sopt.official.config.messaging

import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.RemoteMessage
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.config.FcmPushTokenManager
import org.sopt.official.feature.notification.NotificationAnalyticsEvent
import org.sopt.official.feature.notification.NotificationAnalyticsPropertyKey
import org.sopt.official.feature.notification.SchemeActivity
import org.sopt.official.feature.notification.toNotificationLinkType
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.toViewType
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SoptFirebaseMessagingService : LifecycleAwareFirebaseMessagingService() {

    @Inject
    lateinit var fcmPushTokenManager: FcmPushTokenManager

    @Inject
    lateinit var tracker: Tracker

    @Inject
    lateinit var userStorage: UserStorage

    // 토큰이 갱신되는 시점에만 재등록
    override fun onNewToken(token: String) {
        lifecycleScope.launch {
            runCatching {
                fcmPushTokenManager.registerPushTokenIfAuthenticated(token)
            }.onSuccess {
                Timber.d("갱신된 FCM 토큰 서버 등록 성공")
            }.onFailure {
                Timber.e(it, "갱신된 FCM 토큰 서버 등록 실패")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isEmpty()) return

        val receivedData = remoteMessage.data
        val notificationId = receivedData["id"] ?: ""
        val title = receivedData["title"] ?: ""
        val body = receivedData["content"] ?: ""
        val webLink = receivedData["webLink"]?.takeIf { it != "null" } ?: ""
        val deepLink = receivedData["deepLink"]?.takeIf { it != "null" } ?: ""
        val link = webLink.ifBlank { deepLink }

        lifecycleScope.launch {
            suspendRunCatching {
                tracker.trackViewType(
                    event = NotificationAnalyticsEvent.RECEIVED_PUSH,
                    viewType = userStorage.userStatus.first().toViewType(),
                    properties = buildMap {
                        notificationId.takeIf(String::isNotBlank)?.let {
                            put(NotificationAnalyticsPropertyKey.NOTIFICATION_ID, it)
                        }
                        put(
                            NotificationAnalyticsPropertyKey.NOTIFICATION_LINK_TYPE,
                            link.toNotificationLinkType().value,
                        )
                    },
                )
            }.onFailure { exception ->
                Timber.e(exception, "received_push 전송 실패")
            }
        }

        val notifyId = System.currentTimeMillis().toInt()
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(title).setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body)).setSmallIcon(R.drawable.img_logo_small)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setChannelId(getString(R.string.toolbar_notification)).setAutoCancel(true)

        notificationBuilder.setNotificationContentIntent(
            notificationId, link, notifyId
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifyId, notificationBuilder.build())
    }

    private fun NotificationCompat.Builder.setNotificationContentIntent(
        notificationId: String, link: String, notifyId: Int
    ): NotificationCompat.Builder {
        val intent = SchemeActivity.getIntent(
            this@SoptFirebaseMessagingService,
            SchemeActivity.Argument(
                notificationId = notificationId,
                link = link,
            ),
            isPush = true,
        )

        return this.setContentIntent(
            PendingIntent.getActivity(
                this@SoptFirebaseMessagingService, notifyId, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                }
            )
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "SOPT"
    }
}
