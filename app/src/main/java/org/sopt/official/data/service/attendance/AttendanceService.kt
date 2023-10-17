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
package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceService {
    @GET("/api/v1/app/lectures")
    suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse>

    @GET("/api/v1/app/members/attendances")
    suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse>

    @GET("/api/v1/app/lectures/round/{lectureId}")
    suspend fun getAttendanceRound(
        @Path("lectureId") lectureId: Long
    ): BaseAttendanceResponse<AttendanceRoundResponse>

    @POST("/api/v1/app/attendances/attend")
    suspend fun confirmAttendanceCode(
        @Body param: RequestAttendanceCode
    ): BaseAttendanceResponse<AttendanceCodeResponse>
}
