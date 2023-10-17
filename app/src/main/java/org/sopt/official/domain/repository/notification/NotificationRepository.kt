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
package org.sopt.official.domain.repository.notification

import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
import org.sopt.official.domain.entity.notification.NotificationHistoryItem

interface NotificationRepository {

    suspend fun registerToken(pushToken: String): Result<UpdatePushTokenResponse>
    suspend fun deleteToken(pushToken: String): Result<UpdatePushTokenResponse>

    suspend fun getNotificationHistory(
        page: Int
    ): Result<List<NotificationHistoryItem>>
    suspend fun getNotificationDetail(
        notificationId: Long
    ): Result<NotificationDetailResponse>

    suspend fun updateNotificationReadingState(
        notificationId: Long
    ): Result<NotificationReadingStateResponse>
    suspend fun updateEntireNotificationReadingState(): Result<NotificationReadingStateResponse>
}
