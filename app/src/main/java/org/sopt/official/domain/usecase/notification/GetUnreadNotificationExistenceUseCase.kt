package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class GetUnreadNotificationExistenceUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<UnreadNotificationExistenceResponse> {
        return notificationRepository.getUnreadNotificationExistence()
    }
}
