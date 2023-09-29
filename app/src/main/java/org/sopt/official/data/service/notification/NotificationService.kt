package org.sopt.official.data.service.notification

import org.sopt.official.data.model.attendance.*
import org.sopt.official.data.model.notification.request.NotificationSubscriptionRequest
import org.sopt.official.data.model.notification.request.UpdatePushTokenRequest
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.data.model.notification.response.NotificationReadingStateResponse
import org.sopt.official.data.model.notification.response.NotificationSubscriptionResponse
import org.sopt.official.data.model.notification.response.UnreadNotificationExistenceResponse
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
    ): Result<Unit>

    @DELETE("user/push-token")
    suspend fun deleteToken(
        @Body body: UpdatePushTokenRequest
    ): Result<Unit>


    @GET("notification")
    suspend fun getNotificationHistory(
        @Query("page") page: Int
    ): NotificationHistoryItemResponse

    @GET("notification/main")
    suspend fun getUnreadNotificationExistence(
    ): UnreadNotificationExistenceResponse


    @PATCH("notification/{notificationId}")
    suspend fun updateNotificationReadingState(
        @Path("notificationId") notificationId: Int
    ): NotificationReadingStateResponse

    @PATCH("notification/0")
    suspend fun updateEntireNotificationReadingState(
    ): NotificationReadingStateResponse


    @GET("user/opt-in")
    suspend fun getNotificationSubscription(
    ): NotificationSubscriptionResponse

    @PATCH("user/opt-in")
    suspend fun updateNotificationSubscription(
        @Body body: NotificationSubscriptionRequest
    ): NotificationSubscriptionResponse
}