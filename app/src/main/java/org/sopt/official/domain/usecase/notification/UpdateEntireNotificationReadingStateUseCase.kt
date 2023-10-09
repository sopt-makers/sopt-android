package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class UpdateEntireNotificationReadingStateUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<NotificationReadingStateResponse> {
        return notificationRepository.updateEntireNotificationReadingState()
    }
}
