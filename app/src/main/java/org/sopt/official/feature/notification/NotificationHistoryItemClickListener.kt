package org.sopt.official.feature.notification

import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse

interface NotificationHistoryItemClickListener {
    fun onClickNotificationHistoryItem(position: Int)
}