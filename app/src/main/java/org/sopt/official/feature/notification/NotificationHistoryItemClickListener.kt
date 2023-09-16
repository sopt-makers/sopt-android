package org.sopt.official.feature.notification

import org.sopt.official.data.model.notification.response.NotificationHistoryItem

interface NotificationHistoryItemClickListener {
    fun onClickNotificationHistoryItem(item: NotificationHistoryItem)
}