package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class RegisterPushTokenUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(pushToken: String): Result<UpdatePushTokenResponse> {
        return notificationRepository.registerToken(pushToken)
    }
}
