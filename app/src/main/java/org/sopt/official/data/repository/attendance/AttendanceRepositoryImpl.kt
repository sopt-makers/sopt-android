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