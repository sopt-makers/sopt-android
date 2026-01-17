/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.RemoteMessage
import com.skydoves.firebase.messaging.lifecycle.ktx.LifecycleAwareFirebaseMessagingService
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch
import org.sopt.official.App
import org.sopt.official.R
import org.sopt.official.model.UserStatus
import org.sopt.official.domain.notification.usecase.RegisterPushTokenUseCase
import org.sopt.official.feature.notification.SchemeActivity
import org.sopt.official.network.persistence.SoptDataStore

class SoptFirebaseMessagingService : LifecycleAwareFirebaseMessagingService() {

    @Inject lateinit var dataStore: SoptDataStore
    @Inject lateinit var registerPushTokenUseCase: RegisterPushTokenUseCase

    override fun onCreate() {
        super.onCreate()
        (applicationContext as App).appGraph.inject(this)
    }

  override fun onNewToken(token: String) {
    if (dataStore.userStatus == UserStatus.UNAUTHENTICATED.name) return
    lifecycleScope.launch {
      dataStore.pushToken = token
      registerPushTokenUseCase.invoke(token)
    }
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    if (remoteMessage.data.isEmpty()) return

    val receivedData = remoteMessage.data
    val notificationId = receivedData["id"] ?: ""
    val title = receivedData["title"] ?: ""
    val body = receivedData["content"] ?: ""
    val webLink = receivedData["webLink"] ?: ""
    val deepLink = receivedData["deepLink"] ?: ""

    val notifyId = System.currentTimeMillis().toInt()
    val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(title).setContentText(body)
      .setStyle(NotificationCompat.BigTextStyle().bigText(body)).setSmallIcon(R.drawable.img_logo_small)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setChannelId(getString(R.string.toolbar_notification)).setAutoCancel(true)

    notificationBuilder.setNotificationContentIntent(
      notificationId, webLink.ifBlank { deepLink.ifBlank { "" } }, notifyId
    )

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notifyId, notificationBuilder.build())
  }

  private fun NotificationCompat.Builder.setNotificationContentIntent(
    notificationId: String, link: String, notifyId: Int
  ): NotificationCompat.Builder {
    val intent = SchemeActivity.getIntent(
      this@SoptFirebaseMessagingService, SchemeActivity.Argument(notificationId, link)
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