package org.sopt.official.domain.repository.notification

import org.sopt.official.data.model.notification.request.NotificationSubscriptionRequest
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse

interface NotificationRepository {

    suspend fun registerToken(pushToken: String): Result<Unit>
    suspend fun deleteToken(): Result<Unit>

    suspend fun getNotificationHistory(
        page: Int
    ): Result<NotificationHistoryItemResponse>
    suspend fun getUnreadNotificationExistence(): Result<UnreadNotificationExistenceResponse>
    suspend fun updateNotificationReadingState(
        notificationId: Int
    ): Result<NotificationReadingStateResponse>
    suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse>

    suspend fun getNotificationSubscription(): Result<NotificationSubscriptionResponse>
    suspend fun updateNotificationSubscription(
        notificationSubscriptionRequest: NotificationSubscriptionRequest
    ): Result<NotificationSubscriptionResponse>
}