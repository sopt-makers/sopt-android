package org.sopt.official.data.mapper

import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem

class NotificationHistoryItemMapper {
    fun toNotificationHistoryItem(responseItem: NotificationHistoryItemResponse): NotificationHistoryItem {
        return NotificationHistoryItem(
            id = responseItem.id,
            userId = responseItem.userId,
            title = responseItem.title,
            content = responseItem.content,
            type = responseItem.type,
            isRead = responseItem.isRead,
            createdAt = responseItem.createdAt,
            updatedAt = responseItem.updatedAt
        )
    }
}