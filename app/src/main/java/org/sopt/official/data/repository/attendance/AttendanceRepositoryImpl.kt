package org.sopt.official.data.repository.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.*
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceService: AttendanceService
) : AttendanceRepository {
    override suspend fun fetchSoptEvent(): Result<SoptEvent> = runCatching { attendanceService.getSoptEvent().data!!.toEntity() }
    override suspend fun fetchAttendanceHistory(): Result<AttendanceHistory> =
        runCatching { attendanceService.getAttendanceHistory().data!!.toEntity() }

    override suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound> = runCatching {
        attendanceService.getAttendanceRound(lectureId).data?.toEntity() ?: AttendanceButtonType.ERROR.attendanceRound
    }.recoverCatching {
        AttendanceButtonType.of(it.message ?: "")
    }

    override suspend fun confirmAttendanceCode(
        subLectureId: Long,
        code: String
    ): Result<AttendanceCodeResponse> = runCatching {
        attendanceService.confirmAttendanceCode(RequestAttendanceCode(subLectureId, code)).data ?: AttendanceCodeResponse(-1)
    }.recoverCatching {
        AttendanceErrorCode.of(it.message ?: "") ?: AttendanceCodeResponse(-2)
    }
}