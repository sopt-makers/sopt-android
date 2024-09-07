package org.sopt.official.domain.notification.usecase

import org.sopt.official.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class UpdateEntireNotificationReadingStateUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke() {
        notificationRepository.updateEntireNotificationReadingState()
    }
}
