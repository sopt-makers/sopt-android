package org.sopt.official.domain.usecase.notification

import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class DeletePushTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(pushToken: String): Result<Unit> {
        return notificationRepository.deleteToken(pushToken)
    }
}
