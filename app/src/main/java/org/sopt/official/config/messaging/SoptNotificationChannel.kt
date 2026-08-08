package org.sopt.official.config.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import org.sopt.official.R

object SoptNotificationChannel {
    fun id(context: Context): String = context.getString(R.string.default_channel_id)

    fun create(context: Context) {
        val channel = NotificationChannel(
            id(context),
            context.getString(R.string.toolbar_notification),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableLights(false)
            enableVibration(false)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}
