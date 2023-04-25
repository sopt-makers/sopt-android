package org.sopt.official.domain.entity.attendance

import org.sopt.official.data.model.attendance.AttendanceCodeResponse

enum class AttendanceErrorCode(val attendanceErrorCode: AttendanceCodeResponse) {
    WrongCode(AttendanceCodeResponse(-2)),
    BeforeAttendance(AttendanceCodeResponse(-1)),
    AfterAttendance(AttendanceCodeResponse(0))
}