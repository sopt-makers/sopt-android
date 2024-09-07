package org.sopt.official.domain.notification.usecase

import org.sopt.official.domain.notification.entity.Notification
import org.sopt.official.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationDetailUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Notification> {
        return notificationRepository.getNotificationDetail(notificationId)
    }
}
