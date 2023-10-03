package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class GetNotificationDetailUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: Int): Result<NotificationDetailResponse> {
        return notificationRepository.getNotificationDetail(notificationId)
    }
}
