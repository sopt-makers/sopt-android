package org.sopt.official.data.mapper

import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem

class NotificationHistoryItemMapper {
    fun toNotificationHistoryItem(responseItem: NotificationHistoryItemResponse): NotificationHistoryItem {
        return NotificationHistoryItem(
            notificationId = responseItem.notificationId,
            userId = responseItem.userId,
            title = responseItem.title,
            content = responseItem.content,
            category = responseItem.category,
            isRead = responseItem.isRead,
            createdAt = responseItem.createdAt
        )
    }
}