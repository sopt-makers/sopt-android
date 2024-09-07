package org.sopt.official.domain.notification.usecase

import org.sopt.official.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class DeletePushTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(pushToken: String) {
        return notificationRepository.deleteToken(pushToken)
    }
}
