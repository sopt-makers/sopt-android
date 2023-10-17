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
package org.sopt.official.data.repository.notfication

import org.sopt.official.data.mapper.NotificationHistoryItemMapper
import org.sopt.official.data.model.notification.request.UpdatePushTokenRequest
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.data.service.notification.NotificationService
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.repository.notification.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val service: NotificationService
) : NotificationRepository {

    private val notificationHistoryMapper = NotificationHistoryItemMapper()

    override suspend fun registerToken(pushToken: String): Result<UpdatePushTokenResponse> {
        return runCatching {
            service.registerToken(
                UpdatePushTokenRequest(
                    platform = "Android",
                    pushToken = pushToken
                )
            )
        }
    }

    override suspend fun deleteToken(pushToken: String): Result<UpdatePushTokenResponse> {
        return runCatching {
            service.deleteToken(
                UpdatePushTokenRequest(
                    platform = "Android",
                    pushToken = pushToken
                )
            )
        }
    }

    override suspend fun getNotificationHistory(
        page: Int
    ): Result<List<NotificationHistoryItem>> {
        return runCatching {
            service.getNotificationHistory(page).map(notificationHistoryMapper::toNotificationHistoryItem)
        }
    }

    override suspend fun getNotificationDetail(
        notificationId: Long
    ): Result<NotificationDetailResponse> {
        return runCatching {
            service.getNotificationDetail(notificationId)
        }
    }

    override suspend fun updateNotificationReadingState(
        notificationId: Long
    ): Result<NotificationReadingStateResponse> {
        return runCatching {
            service.updateNotificationReadingState(notificationId)
        }
    }

    override suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse> {
        return runCatching {
            service.updateEntireNotificationReadingState()
        }
    }
}
