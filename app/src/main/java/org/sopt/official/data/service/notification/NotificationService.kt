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
package org.sopt.official.data.service.notification

import org.sopt.official.data.model.attendance.*
import org.sopt.official.data.model.notification.request.UpdatePushTokenRequest
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.UpdatePushTokenResponse
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

    @GET("notification")
    suspend fun getNotificationHistory(
        @Query("page") page: Int
    ): ArrayList<NotificationHistoryItemResponse>

    @GET("notification/{notificationId}")
    suspend fun getNotificationDetail(
        @Path("notificationId") notificationId: Long
    ): NotificationDetailResponse

    @PATCH("notification/{notificationId}")
    suspend fun updateNotificationReadingState(
        @Path("notificationId") notificationId: Long
    ): NotificationReadingStateResponse

    @PATCH("notification")
    suspend fun updateEntireNotificationReadingState(): NotificationReadingStateResponse
}
