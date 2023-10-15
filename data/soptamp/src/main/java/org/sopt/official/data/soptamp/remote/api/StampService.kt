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
package org.sopt.official.data.soptamp.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sopt.official.data.soptamp.remote.model.response.ModifyStampResponse
import org.sopt.official.data.soptamp.remote.model.response.StampResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StampService {
    @GET("stamp")
    suspend fun retrieveStamp(
        @Query("missionId") missionId: Int,
        @Query("nickname") nickname: String,
    ): StampResponse

    @Multipart
    @PUT("stamp/{missionId}")
    suspend fun modifyStamp(
        @Path("missionId") missionId: Int,
        @Part("stampContent") stampContent: RequestBody,
        @Part imgUrl: List<MultipartBody.Part>? = null
    ): ModifyStampResponse

    @Multipart
    @POST("stamp/{missionId}")
    suspend fun registerStamp(
        @Path("missionId") missionId: Int,
        @Part("stampContent") stampContent: RequestBody,
        @Part imgUrl: List<MultipartBody.Part>? = null
    ): StampResponse

    @DELETE("stamp/{missionId}")
    suspend fun deleteStamp(@Path("missionId") missionId: Int)

    @DELETE("stamp/all")
    suspend fun deleteAllStamps(): Result<Unit>
}
