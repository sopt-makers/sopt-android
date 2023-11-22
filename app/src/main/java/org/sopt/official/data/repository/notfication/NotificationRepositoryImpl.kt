/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
        notificationId: String
    ): Result<NotificationDetailResponse> {
        return runCatching {
            service.getNotificationDetail(notificationId)
        }
    }

    override suspend fun updateNotificationReadingState(
        notificationId: String
    ) {
        runCatching {
            service.updateNotificationReadingState(notificationId)
        }
    }

    override suspend fun updateEntireNotificationReadingState() {
        runCatching {
            service.updateEntireNotificationReadingState()
        }
    }
}
