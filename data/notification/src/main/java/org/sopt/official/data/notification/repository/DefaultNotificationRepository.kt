/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.notification.repository

import dev.zacsweers.metro.Inject
import org.sopt.official.data.notification.model.request.UpdatePushTokenRequest
import org.sopt.official.data.notification.service.NotificationService
import org.sopt.official.domain.notification.entity.Notification
import org.sopt.official.domain.notification.entity.NotificationItem
import org.sopt.official.domain.notification.repository.NotificationRepository

@Inject
class DefaultNotificationRepository(
  private val service: NotificationService
) : NotificationRepository {
  override suspend fun registerToken(pushToken: String) {
    service.registerToken(
      UpdatePushTokenRequest(
        platform = "Android", pushToken = pushToken
      )
    )
  }

  override suspend fun deleteToken(pushToken: String) {
    service.deleteToken(
      UpdatePushTokenRequest(
        platform = "Android", pushToken = pushToken
      )
    )
  }

  override suspend fun getNotificationHistory(page: Int): Result<List<NotificationItem>> {
    return runCatching {
      service.getNotificationHistory(page).map { it.asDomain() }
    }
  }

    override suspend fun getNotificationHistoryByCategory(page: Int, category: String): Result<List<NotificationItem>> {
        return runCatching {
            service.getNotificationHistoryByCategory(page = page, category = category).map { it.asDomain() }
        }
    }

  override suspend fun getNotificationDetail(notificationId: String): Result<Notification> {
    return runCatching {
      service.getNotificationDetail(notificationId).asDomain()
    }
  }

  override suspend fun updateNotificationReadingState(notificationId: String) {
    service.updateNotificationReadingState(notificationId)
  }

  override suspend fun updateEntireNotificationReadingState() {
    service.updateEntireNotificationReadingState()
  }
}
