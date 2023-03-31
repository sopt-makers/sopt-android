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
package org.sopt.stamp.data.remote.api

import org.sopt.official.stamp.data.remote.model.response.MissionResponse
import retrofit2.http.GET
import retrofit2.http.Header

internal interface SoptampService {
    @GET("mission/all")
    suspend fun getAllMissions(@Header("userId") userId: Int): List<MissionResponse>

    @GET("mission/complete")
    suspend fun getCompleteMissions(@Header("userId") userId: Int): List<MissionResponse>

    @GET("mission/incomplete")
    suspend fun getIncompleteMissions(@Header("userId") userId: Int): List<MissionResponse>
}
