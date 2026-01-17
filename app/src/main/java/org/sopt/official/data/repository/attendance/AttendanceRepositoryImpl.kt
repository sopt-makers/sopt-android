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
package org.sopt.official.data.repository.attendance

import dev.zacsweers.metro.Inject
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

@Inject
class AttendanceRepositoryImpl(
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

    override suspend fun confirmAttendanceCode(subLectureId: Long, code: String): Result<AttendanceCodeResponse> = runCatching {
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
