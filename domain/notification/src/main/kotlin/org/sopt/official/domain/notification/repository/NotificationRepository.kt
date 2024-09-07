package org.sopt.official.domain.notification.repository

import org.sopt.official.domain.notification.entity.Notification
import org.sopt.official.domain.notification.entity.NotificationItem

interface NotificationRepository {
    suspend fun registerToken(pushToken: String)
    suspend fun deleteToken(pushToken: String)
    suspend fun getNotificationHistory(page: Int): Result<List<NotificationItem>>
    suspend fun getNotificationDetail(notificationId: String): Result<Notification>
    suspend fun updateNotificationReadingState(notificationId: String)
    suspend fun updateEntireNotificationReadingState()
}
