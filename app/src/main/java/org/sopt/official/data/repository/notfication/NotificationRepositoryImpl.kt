package org.sopt.official.data.repository.notfication

import org.sopt.official.data.model.notification.request.NotificationSubscriptionRequest
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.data.service.notification.NotificationService
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val service: NotificationService
) : NotificationRepository {

    override suspend fun registerToken(pushToken: String): Result<Unit> {
        return runCatching {
            service.registerToken(pushToken)
        }
    }

    override suspend fun deleteToken(): Result<Unit> {
        return runCatching {
            service.deleteToken()
        }
    }

    override suspend fun getNotificationHistory(
        page: Int
    ): Result<NotificationHistoryItemResponse> {
        return runCatching {
            service.getNotificationHistory(page)
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
        notificationSubscriptionRequest: NotificationSubscriptionRequest
    ): Result<NotificationSubscriptionResponse> {
        return runCatching {
            service.updateNotificationSubscription(notificationSubscriptionRequest)
        }
    }

}
