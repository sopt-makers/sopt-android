package org.sopt.official.data.repository.attendance

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sopt.official.data.mapToAttendance
import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.AttendanceRoundResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.model.attendance.SoptEventResponse
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.ConfirmAttendanceCodeResult
import org.sopt.official.domain.entity.attendance.FetchAttendanceCurrentRoundResult
import org.sopt.official.domain.repository.attendance.NewAttendanceRepository
import retrofit2.HttpException
import javax.inject.Inject

class DefaultAttendanceRepository @Inject constructor(
    private val attendanceService: AttendanceService,
    private val json: Json
) : NewAttendanceRepository {
    override suspend fun fetchAttendanceInfo(): Attendance {
        val soptEventResponse: SoptEventResponse? = runCatching { attendanceService.getSoptEvent().data }.getOrNull()
        val attendanceHistoryResponse: AttendanceHistoryResponse? =
            runCatching { attendanceService.getAttendanceHistory().data }.getOrNull()

        val attendance: Attendance =
            mapToAttendance(attendanceHistoryResponse = attendanceHistoryResponse, soptEventResponse = soptEventResponse)
        return attendance
    }

    override suspend fun fetchAttendanceCurrentRound(lectureId: Long): FetchAttendanceCurrentRoundResult {
        return runCatching { attendanceService.getAttendanceRound(lectureId).data }.fold(
            onSuccess = { attendanceRoundResponse: AttendanceRoundResponse? ->
                FetchAttendanceCurrentRoundResult.Success(attendanceRoundResponse?.round)
            },
            onFailure = { error: Throwable ->
                if (error !is HttpException) return FetchAttendanceCurrentRoundResult.Failure(null)

                val message: String? = error.jsonErrorMessage
                FetchAttendanceCurrentRoundResult.Failure(message)
            },
        )
    }

    override suspend fun confirmAttendanceCode(
        subLectureId: Long,
        code: String
    ): ConfirmAttendanceCodeResult {
        return runCatching {
            attendanceService.confirmAttendanceCode(RequestAttendanceCode(subLectureId = subLectureId, code = code))
        }.fold(
            onSuccess = { ConfirmAttendanceCodeResult.Success },
            onFailure = { error: Throwable ->
                if (error !is HttpException) return ConfirmAttendanceCodeResult.Failure(null)

                val message: String? = error.jsonErrorMessage
                ConfirmAttendanceCodeResult.Failure(message)
            },
        )
    }

    private val HttpException.jsonErrorMessage: String?
        get() {
            val errorBody: String = this.response()?.errorBody()?.string() ?: return null
            val jsonObject: JsonObject = json.parseToJsonElement(errorBody).jsonObject
            val errorMessage: String? = jsonObject["message"]?.jsonPrimitive?.contentOrNull
            return errorMessage
        }
}