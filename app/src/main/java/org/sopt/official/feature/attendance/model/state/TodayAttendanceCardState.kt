package org.sopt.official.feature.attendance.model.state

import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance

class TodayAttendanceCardState(
    val eventDate: String,
    val eventLocation: String,
    val eventName: String,
    val firstAttendance: MidtermAttendance,
    val secondAttendance: MidtermAttendance,
    val finalAttendance: FinalAttendance,
)