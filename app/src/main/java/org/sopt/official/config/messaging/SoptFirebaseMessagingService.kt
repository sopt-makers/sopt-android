package org.sopt.official.config.messaging

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.usecase.notification.RegisterPushTokenUseCase
import org.sopt.official.feature.auth.AuthActivity
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SoptFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: SoptDataStore

    @Inject
    lateinit var registerPushTokenUseCase: RegisterPushTokenUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val channelId = "SOPT"


    override fun onNewToken(token: String) {
        if (dataStore.userStatus == UserStatus.UNAUTHENTICATED.name) return
        scope.launch {
            dataStore.pushToken = token
            registerPushTokenUseCase.invoke(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isEmpty()) return

        val receivedData = remoteMessage.data
        val title = receivedData["title"] ?: ""
        val body = receivedData["content"] ?: ""
        val webLink = receivedData["webLink"] ?: ""
        val deepLink = receivedData["deepLink"] ?: ""
        Timber.tag("SOPT").e("onMessageReceived - title: %s", title)

        val notificationId = System.currentTimeMillis().toInt()
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSmallIcon(R.drawable.img_logo_small)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId(getString(R.string.toolbar_notification))
            .setAutoCancel(true)

        notificationBuilder.setNotificationContentIntent(
            if (webLink.isNotBlank()) {
                RemoteMessageLinkType.WEB_LINK
            } else if (deepLink.isNotBlank()) {
                RemoteMessageLinkType.DEEP_LINK
            } else {
                RemoteMessageLinkType.DEFAULT
            },
            webLink.ifBlank { deepLink.ifBlank { "" } },
            notificationId
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())

        Timber.e("onMessageReceived: %s", remoteMessage)
        Timber.e("onMessageReceived - notification: " + remoteMessage.notification)
        Timber.e("onMessageReceived - notification title: " + remoteMessage.notification?.title)
        Timber.e("onMessageReceived - notification body: " + remoteMessage.notification?.body)
        Timber.e("onMessageReceived - data: " + remoteMessage.data)
        Timber.e("onMessageReceived - data entries: " + remoteMessage.data.entries)
        Timber.d("SOPT", "----------------------------")
        Timber.d("SOPT", "received message: $remoteMessage")
    }

    private fun NotificationCompat.Builder.setNotificationContentIntent(
        remoteMessageLinkType: RemoteMessageLinkType,
        link: String,
        notificationId: Int
    ): NotificationCompat.Builder {

        val intent = Intent(this@SoptFirebaseMessagingService, AuthActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(REMOTE_MESSAGE_EVENT_TYPE, remoteMessageLinkType.name)
            putExtra(REMOTE_MESSAGE_EVENT_LINK, link)
        }

        return this.setContentIntent(
            PendingIntent.getActivity(
                this@SoptFirebaseMessagingService,
                notificationId,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    companion object {
        const val REMOTE_MESSAGE_EVENT_TYPE = "REMOTE_MESSAGE_EVENT_TYPE"
        const val REMOTE_MESSAGE_EVENT_LINK = "REMOTE_MESSAGE_EVENT_LINK"
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
