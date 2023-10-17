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
package org.sopt.official.data.repository.attendance

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sopt.official.data.model.attendance.AttendanceCodeResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.AttendanceButtonType
import org.sopt.official.domain.entity.attendance.AttendanceErrorCode
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceRound
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import retrofit2.HttpException
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceService: AttendanceService,
    private val json: Json
) : AttendanceRepository {
    override suspend fun fetchSoptEvent(): Result<SoptEvent> = runCatching { attendanceService.getSoptEvent().data!!.toEntity() }
    override suspend fun fetchAttendanceHistory(): Result<AttendanceHistory> =
        runCatching { attendanceService.getAttendanceHistory().data!!.toEntity() }

    override suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound> = runCatching {
        attendanceService.getAttendanceRound(lectureId).data?.toEntity() ?: AttendanceButtonType.ERROR.attendanceRound
    }.recoverCatching { cause ->
        when (cause) {
            is HttpException -> {
                val errorBodyString = cause.response()?.errorBody()?.string()
                if (errorBodyString != null) {
                    val errorBody = json.parseToJsonElement(errorBodyString).jsonObject
                    val message = errorBody["message"]?.jsonPrimitive?.contentOrNull
                    AttendanceButtonType.of(message ?: "")
                } else {
                    AttendanceRound.ERROR
                }
            }

            else -> AttendanceRound.ERROR
        }
    }

    override suspend fun confirmAttendanceCode(
        subLectureId: Long,
        code: String
    ): Result<AttendanceCodeResponse> = runCatching {
        attendanceService.confirmAttendanceCode(RequestAttendanceCode(subLectureId, code)).data ?: AttendanceCodeResponse(-1)
    }.recoverCatching { cause ->
        when (cause) {
            is HttpException -> {
                val errorBodyString = cause.response()?.errorBody()?.string()
                if (errorBodyString != null) {
                    val errorBody = json.parseToJsonElement(errorBodyString).jsonObject
                    val message = errorBody["message"]?.jsonPrimitive?.contentOrNull
                    AttendanceErrorCode.of(message ?: "") ?: AttendanceCodeResponse.ERROR
                } else {
                    AttendanceCodeResponse.ERROR
                }
            }

            else -> AttendanceCodeResponse.ERROR
        }
    }
}
