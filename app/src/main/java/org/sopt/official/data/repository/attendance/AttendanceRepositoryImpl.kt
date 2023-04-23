package org.sopt.official.data.repository.attendance

import android.util.Log
import org.sopt.official.data.service.attendance.AttendanceService
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
    override suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound> = kotlin.runCatching {
        return runCatching {
            attendanceService.getAttendanceRound(lectureId)
        }.fold(
            {
                when (it.message) {
                    "[LectureException] : 오늘 세션이 없습니다." -> Result.success(AttendanceRound(-1, ""))
                    "[LectureException] : 출석 시작 전입니다." -> Result.success(AttendanceRound(0, "1차 출석 전"))
                    "[LectureException] : 1차 출석 시작 전입니다." -> Result.success(AttendanceRound(0, "1차 출석 전"))
                    "[LectureException] : 2차 출석 시작 전입니다." -> Result.success(AttendanceRound(0, "2차 출석 전"))
                    "[LectureException] : 1차 출석이 이미 종료되었습니다." -> Result.success(AttendanceRound(0, "2차 출석 전"))
                    "[LectureException] : 2차 출석이 이미 종료되었습니다." -> Result.success(AttendanceRound(0, "2차 출석 종료"))
                    else -> it.data?.let { data -> Result.success(data.toEntity()) } ?: Result.success(AttendanceRound(-2, ""))
                }
            },
            {
                it.printStackTrace()
                Result.failure(it.fillInStackTrace())
            }
        )
    }
}