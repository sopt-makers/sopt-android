package org.sopt.official.domain.repository.notification

import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem

interface NotificationRepository {

    suspend fun registerToken(pushToken: String): Result<UpdatePushTokenResponse>
    suspend fun deleteToken(pushToken: String): Result<UpdatePushTokenResponse>

    suspend fun getNotificationHistory(
        page: Int
    ): Result<List<NotificationHistoryItem>>
    suspend fun getUnreadNotificationExistence(): Result<UnreadNotificationExistenceResponse>
    suspend fun updateNotificationReadingState(
        notificationId: Int
    ): Result<NotificationReadingStateResponse>
    suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse>

    suspend fun getNotificationSubscription(): Result<NotificationSubscriptionResponse>
    suspend fun updateNotificationSubscription(
        isSubscribed: Boolean
    ): Result<NotificationSubscriptionResponse>
}