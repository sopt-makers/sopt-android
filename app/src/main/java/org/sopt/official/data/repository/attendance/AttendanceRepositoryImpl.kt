package org.sopt.official.data.repository.attendance

import org.json.JSONObject
import org.sopt.official.data.model.attendance.AttendanceCodeResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.*
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import retrofit2.HttpException
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceService: AttendanceService
) : AttendanceRepository {
    override suspend fun fetchSoptEvent(): Result<SoptEvent> = runCatching { attendanceService.getSoptEvent().data!!.toEntity() }
    override suspend fun fetchAttendanceHistory(): Result<AttendanceHistory> =
        runCatching { attendanceService.getAttendanceHistory().data!!.toEntity() }

    override suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound> = runCatching {
        attendanceService.getAttendanceRound(lectureId).data?.toEntity() ?: AttendanceButtonType.ERROR.attendanceRound
    }.recoverCatching { cause ->
        when (cause) {
            is HttpException -> {
                val errorBody = cause.response()?.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }
                val message = jsonObject?.getString("message")
                AttendanceButtonType.of(message ?: "")
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
                val errorBody = cause.response()?.errorBody()?.string()
                val jsonObject = errorBody?.let { JSONObject(it) }
                val message = jsonObject?.getString("message")
                AttendanceErrorCode.of(message ?: "") ?: AttendanceCodeResponse(-2)
            }

            else -> AttendanceCodeResponse.ERROR
        }
    }
}