package org.sopt.official.data.repository.attendance

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.AttendanceHistoryResponse.AttendanceResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.model.attendance.SoptEventResponse
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance
import org.sopt.official.domain.entity.attendance.Attendance.AttendanceDayType.HasAttendance.RoundAttendance.RoundAttendanceState
import org.sopt.official.domain.entity.attendance.Attendance.SessionInfo
import org.sopt.official.domain.entity.attendance.Attendance.User.AttendanceLog.AttendanceState
import org.sopt.official.domain.entity.attendance.ConfirmAttendanceCodeResult
import org.sopt.official.domain.entity.attendance.FetchAttendanceCurrentRoundResult
import org.sopt.official.domain.repository.attendance.NewAttendanceRepository
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultAttendanceRepository @Inject constructor(
    private val attendanceService: AttendanceService,
    private val json: Json
) : NewAttendanceRepository {
    override suspend fun fetchAttendanceInfo(): Attendance {
        val soptEventResponse: SoptEventResponse? = runCatching { attendanceService.getSoptEvent().data }.getOrNull()
        val attendanceHistoryResponse: AttendanceHistoryResponse? =
            runCatching { attendanceService.getAttendanceHistory().data }.getOrNull()
        return Attendance(
            sessionId = soptEventResponse?.id ?: Attendance.UNKNOWN_SESSION_ID,
            user = Attendance.User(
                name = attendanceHistoryResponse?.name ?: Attendance.User.UNKNOWN_NAME,
                generation = attendanceHistoryResponse?.generation ?: Attendance.User.UNKNOWN_GENERATION,
                part = Attendance.User.Part.valueOf(attendanceHistoryResponse?.part ?: Attendance.User.UNKNOWN_PART),
                attendanceScore = attendanceHistoryResponse?.score ?: 0.0,
                attendanceCount = Attendance.User.AttendanceCount(
                    attendanceCount = attendanceHistoryResponse?.attendanceCount?.normal ?: 0,
                    lateCount = attendanceHistoryResponse?.attendanceCount?.late ?: 0,
                    absenceCount = attendanceHistoryResponse?.attendanceCount?.abnormal ?: 0,
                ),
                attendanceHistory = attendanceHistoryResponse?.attendances?.map { attendanceResponse: AttendanceResponse ->
                    Attendance.User.AttendanceLog(
                        sessionName = attendanceResponse.eventName,
                        date = attendanceResponse.date,
                        attendanceState = AttendanceState.valueOf(attendanceResponse.attendanceState)
                    )
                } ?: emptyList(),
            ),
            attendanceDayType = when (soptEventResponse?.type) {
                "NO_SESSION" -> {
                    AttendanceDayType.NoSession
                }

                "HAS_ATTENDANCE" -> {
                    val firstAttendanceResponse: SoptEventResponse.AttendanceResponse? = soptEventResponse.attendances.firstOrNull()
                    val secondAttendanceResponse: SoptEventResponse.AttendanceResponse? = soptEventResponse.attendances.lastOrNull()
                    AttendanceDayType.HasAttendance(
                        sessionInfo = SessionInfo(
                            sessionName = soptEventResponse.eventName,
                            location = soptEventResponse.location.ifBlank { null },
                            sessionStartTime = LocalDateTime.parse(soptEventResponse.startAt),
                            sessionEndTime = LocalDateTime.parse(soptEventResponse.endAt),
                        ),
                        firstRoundAttendance = RoundAttendance(
                            state = if (firstAttendanceResponse == null) RoundAttendanceState.NOT_YET else RoundAttendanceState.valueOf(
                                firstAttendanceResponse.status
                            ),
                            attendedAt = LocalDateTime.parse(firstAttendanceResponse?.attendedAt),
                        ),
                        secondRoundAttendance = RoundAttendance(
                            state = if (secondAttendanceResponse == null) RoundAttendanceState.NOT_YET else RoundAttendanceState.valueOf(
                                secondAttendanceResponse.status
                            ),
                            attendedAt = LocalDateTime.parse(secondAttendanceResponse?.attendedAt),
                        ),
                    )
                }

                "NO_ATTENDANCE" -> {
                    AttendanceDayType.NoAttendance(
                        sessionInfo = SessionInfo(
                            sessionName = soptEventResponse.eventName,
                            location = soptEventResponse.location.ifBlank { null },
                            sessionStartTime = LocalDateTime.parse(soptEventResponse.startAt),
                            sessionEndTime = LocalDateTime.parse(soptEventResponse.endAt),
                        )
                    )
                }

                else -> {
                    AttendanceDayType.NoSession
                }
            },
        )
    }

    override suspend fun fetchAttendanceCurrentRound(lectureId: Long): FetchAttendanceCurrentRoundResult {
        return runCatching { attendanceService.getAttendanceRound(lectureId).data }.fold(
            onSuccess = { FetchAttendanceCurrentRoundResult.Success(it?.round) },
            onFailure = { error: Throwable ->
                if (error !is HttpException) return FetchAttendanceCurrentRoundResult.Failure(null)

                val errorBodyString: String =
                    error.response()?.errorBody()?.string() ?: return FetchAttendanceCurrentRoundResult.Failure(null)

                val jsonObject = json.parseToJsonElement(errorBodyString).jsonObject
                val errorMessage = jsonObject["message"]?.jsonPrimitive?.contentOrNull

                FetchAttendanceCurrentRoundResult.Failure(errorMessage)
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

                val errorBodyString: String =
                    error.response()?.errorBody()?.string() ?: return ConfirmAttendanceCodeResult.Failure(null)

                val jsonObject = json.parseToJsonElement(errorBodyString).jsonObject
                val errorMessage = jsonObject["message"]?.jsonPrimitive?.contentOrNull

                ConfirmAttendanceCodeResult.Failure(errorMessage)
            },
        )
    }

}
