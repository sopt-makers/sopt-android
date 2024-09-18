/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.notification.service

import org.sopt.official.data.notification.model.request.UpdatePushTokenRequest
import org.sopt.official.data.notification.model.response.NotificationDetailResponse
import org.sopt.official.data.notification.model.response.NotificationHistoryItemResponse
import org.sopt.official.data.notification.model.response.UpdatePushTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
  @POST("user/push-token")
  suspend fun registerToken(
    @Body body: UpdatePushTokenRequest
  ): UpdatePushTokenResponse

  @DELETE("user/push-token")
  suspend fun deleteToken(
    @Body body: UpdatePushTokenRequest
  ): UpdatePushTokenResponse

  @GET("notification/all")
  suspend fun getNotificationHistory(
    @Query("page") page: Int
  ): List<NotificationHistoryItemResponse>

  @GET("notification/detail/{notificationId}")
  suspend fun getNotificationDetail(
    @Path("notificationId") notificationId: String
  ): NotificationDetailResponse

  @PATCH("notification/read/{notificationId}")
  suspend fun updateNotificationReadingState(
    @Path("notificationId") notificationId: String
  )

  @PATCH("notification/read")
  suspend fun updateEntireNotificationReadingState()
}
