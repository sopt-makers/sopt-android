package org.sopt.official.domain.usecase.notification

import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class GetNotificationHistoryUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(page: Int): Result<ArrayList<NotificationHistoryItemResponse>> {
        return notificationRepository.getNotificationHistory(page)
    }
}
