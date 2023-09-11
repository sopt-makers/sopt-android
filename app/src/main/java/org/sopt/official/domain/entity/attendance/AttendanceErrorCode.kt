package org.sopt.official.domain.entity.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse

enum class AttendanceErrorCode(
    val attendanceErrorCode: AttendanceCodeResponse,
    val messages: List<String>
) {
    WRONG_CODE(AttendanceCodeResponse(-2), listOf("[LectureException] : 코드가 일치하지 않아요!")),
    BEFORE_ATTENDANCE(AttendanceCodeResponse(-1), listOf("[LectureException] : 1차 출석 시작 전입니다", "[LectureException] : 2차 출석 시작 전입니다")),
    AFTER_ATTENDANCE(
        AttendanceCodeResponse(0),
        listOf("[LectureException] : 1차 출석이 이미 종료되었습니다.", "[LectureException] : 2차 출석이 이미 종료되었습니다.")
    );

    companion object {
        fun of(message: String) = entries.find { it.messages.contains(message) }?.attendanceErrorCode
    }
}
