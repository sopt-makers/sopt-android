package org.sopt.official.domain.notification.usecase

import org.sopt.official.domain.notification.entity.NotificationItem
import org.sopt.official.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationHistoryUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(page: Int): Result<List<NotificationItem>> {
        return notificationRepository.getNotificationHistory(page)
    }
}
