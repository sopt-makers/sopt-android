package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class UpdateNotificationSubscriptionUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        isSubscribed: Boolean
    ): Result<NotificationSubscriptionResponse> {
        return notificationRepository.updateNotificationSubscription(
            isSubscribed = isSubscribed
        )
    }
}
