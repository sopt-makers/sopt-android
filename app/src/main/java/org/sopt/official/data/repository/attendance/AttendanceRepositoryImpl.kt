package org.sopt.official.data.repository.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse
import org.sopt.official.data.model.attendance.RequestAttendanceCode
import org.sopt.official.data.service.attendance.AttendanceService
import org.sopt.official.domain.entity.attendance.AttendanceButtonType
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceRound
import org.sopt.official.domain.entity.attendance.SoptEvent
import org.sopt.official.domain.repository.attendance.AttendanceRepository
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceService: AttendanceService
) : AttendanceRepository {
    override suspend fun fetchSoptEvent(): Result<SoptEvent> = runCatching { attendanceService.getSoptEvent().data!!.toEntity() }
    override suspend fun fetchAttendanceHistory(): Result<AttendanceHistory> =
        runCatching { attendanceService.getAttendanceHistory().data!!.toEntity() }
    override suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound> = runCatching {
        attendanceService.getAttendanceRound(lectureId)
    }.fold(
        {
            when (it.message) {
                "[LectureException] : 오늘 세션이 없습니다." -> Result.success(AttendanceButtonType.GONE_BUTTON.attendanceRound)
                "[LectureException] : 출석 시작 전입니다." -> Result.success(AttendanceButtonType.BEFORE_FIRST_ATTENDANCE.attendanceRound)
                "[LectureException] : 1차 출석 시작 전입니다." -> Result.success(AttendanceButtonType.BEFORE_FIRST_ATTENDANCE.attendanceRound)
                "[LectureException] : 2차 출석 시작 전입니다." -> Result.success(AttendanceButtonType.BEFORE_SECOND_ATTENDANCE.attendanceRound)
                "[LectureException] : 1차 출석이 이미 종료되었습니다." -> Result.success(AttendanceButtonType.BEFORE_SECOND_ATTENDANCE.attendanceRound)
                "[LectureException] : 2차 출석이 이미 종료되었습니다." -> Result.success(AttendanceButtonType.AFTER_SECOND_ATTENDANCE.attendanceRound)
                else -> it.data?.let { data -> Result.success(data.toEntity()) } ?: Result.success(
                    AttendanceButtonType.ERROR.attendanceRound
                )
            }
        },
        {
            it.printStackTrace()
            Result.failure(it.fillInStackTrace())
        }
    )
    override suspend fun confirmAttendanceCode(subLectureId: Long, code: String): Result<AttendanceCodeResponse> = runCatching {
        attendanceService.confirmAttendanceCode(RequestAttendanceCode(subLectureId, code)).data!!
    }
}