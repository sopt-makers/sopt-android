/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
