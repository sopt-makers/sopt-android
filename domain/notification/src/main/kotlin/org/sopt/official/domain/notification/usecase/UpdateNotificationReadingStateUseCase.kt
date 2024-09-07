package org.sopt.official.domain.notification.usecase

import org.sopt.official.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationReadingStateUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String) {
        notificationRepository.updateNotificationReadingState(notificationId)
    }
}
