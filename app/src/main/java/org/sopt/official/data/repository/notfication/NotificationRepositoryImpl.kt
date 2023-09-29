package org.sopt.official.data.repository.notfication

import org.sopt.official.data.mapper.NotificationHistoryItemMapper
import org.sopt.official.data.model.notification.request.NotificationSubscriptionRequest
import org.sopt.official.data.model.notification.request.UpdatePushTokenRequest
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.data.service.notification.NotificationService
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val service: NotificationService
) : NotificationRepository {

    private val notificationHistoryMapper = NotificationHistoryItemMapper()

    override suspend fun registerToken(pushToken: String): Result<UpdatePushTokenResponse> {
        return runCatching {
            service.registerToken(
                UpdatePushTokenRequest(
                    platform = "Android",
                    pushToken = pushToken
                )
            )
        }
    }

    override suspend fun deleteToken(pushToken: String): Result<UpdatePushTokenResponse> {
        return runCatching {
            service.deleteToken(
                UpdatePushTokenRequest(
                    platform = "Android",
                    pushToken = pushToken
                )
            )
        }
    }

    override suspend fun getNotificationHistory(
        page: Int
    ): Result<List<NotificationHistoryItem>> {
        return runCatching {
            service.getNotificationHistory(page).map(notificationHistoryMapper::toNotificationHistoryItem)
        }
    }

    override suspend fun getUnreadNotificationExistence(): Result<UnreadNotificationExistenceResponse> {
        return runCatching {
            service.getUnreadNotificationExistence()
        }
    }

    override suspend fun updateNotificationReadingState(
        notificationId: Int
    ): Result<NotificationReadingStateResponse> {
        return runCatching {
            service.updateNotificationReadingState(notificationId)
        }
    }

    override suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse> {
        return runCatching {
            service.updateEntireNotificationReadingState()
        }
    }

    override suspend fun getNotificationSubscription(): Result<NotificationSubscriptionResponse> {
        return runCatching {
            service.getNotificationSubscription()
        }
    }

    override suspend fun updateNotificationSubscription(
        isSubscribed: Boolean
    ): Result<NotificationSubscriptionResponse> {
        return runCatching {
            service.updateNotificationSubscription(
                NotificationSubscriptionRequest(
                    allOptIn = isSubscribed,
                    partOptIn = isSubscribed,
                    newsOptIn = isSubscribed
                )
            )
        }
    }
}
