package org.sopt.official.domain.repository.notification

import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem

interface NotificationRepository {

    suspend fun registerToken(pushToken: String): Result<UpdatePushTokenResponse>
    suspend fun deleteToken(pushToken: String): Result<UpdatePushTokenResponse>

    suspend fun getNotificationHistory(
        page: Int
    ): Result<List<NotificationHistoryItem>>
    suspend fun getNotificationDetail(
        notificationId: Int
    ): Result<NotificationDetailResponse>

    suspend fun updateNotificationReadingState(
        notificationId: Int
    ): Result<NotificationReadingStateResponse>
    suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse>
}