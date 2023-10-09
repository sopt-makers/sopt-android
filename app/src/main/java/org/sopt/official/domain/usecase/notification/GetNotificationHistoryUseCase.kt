package org.sopt.official.domain.usecase.notification

import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class GetNotificationHistoryUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(page: Int): Result<List<NotificationHistoryItem>> {
        return notificationRepository.getNotificationHistory(page)
    }
}
