package org.sopt.official.feature.attendance.model

import androidx.annotation.DrawableRes
import org.sopt.official.R

enum class FinalAttendance(
    @DrawableRes val imageResId: Int,
    val isFinished: Boolean,
    val result: String,
) {
    NOT_YET(
        imageResId = R.drawable.ic_attendance_state_nothing,
        isFinished = false,
        result = "출석 전"
    ),
    PRESENT(
        imageResId = R.drawable.ic_attendance_state_done,
        isFinished = true,
        result = "출석완료!"
    ),
    LATE(
        imageResId = R.drawable.ic_attendance_state_late,
        isFinished = true,
        result = "지각"
    ),
    ABSENT(
        imageResId = R.drawable.ic_attendance_state_absence_black,
        isFinished = true,
        result = "결석"
    )
}
