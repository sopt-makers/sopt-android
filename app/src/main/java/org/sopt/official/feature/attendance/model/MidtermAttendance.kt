package org.sopt.official.feature.attendance.model

import androidx.annotation.DrawableRes
import org.sopt.official.R

sealed class MidtermAttendance(
    @DrawableRes val imageResId: Int,
    val isFinished: Boolean,
    val description: String
) {
    data class NotYet(val attendanceType: AttendanceType) : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_nothing,
        isFinished = false,
        description = attendanceType.type
    )

    data class Present(val attendanceAt: String) : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_yes,
        isFinished = true,
        description = attendanceAt
    )

    data object Absent : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_absence_white,
        isFinished = true,
        description = "-"
    )
}