package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.SoptEventResponse

class MockAttendanceService : AttendanceService {
    override suspend fun getSoptEvent(): SoptEventResponse {
        return ATTENDANCE_POINT_AWARDED_EVENT_WITH_LOCATION
    }

    override suspend fun getAttendanceHistory(): AttendanceHistoryResponse {
        return ATTENDANCE_HISTORY
    }

    companion object {
        private val NOT_EVENT_DAY = SoptEventResponse(isEventDay = false)
        private val ATTENDANCE_POINT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
            isEventDay = true,
            date = "3월 23일 토요일 14:00 - 18:00",
            location = "건국대학교 꽥꽥오리관",
            eventName = "2차 세미나",
            isAttendancePointAwardedEvent = true
        )
        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
            isEventDay = true,
            date = "3월 23일 토요일 14:00 - 18:00",
            location = "건국대학교 꽥꽥오리관",
            eventName = "데모데이",
            isAttendancePointAwardedEvent = false
        )
        private val ATTENDANCE_POINT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
            isEventDay = true,
            date = "3월 23일 토요일 14:00 - 18:00",
            location = null,
            eventName = "2차 세미나",
            isAttendancePointAwardedEvent = true
        )
        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
            isEventDay = true,
            date = "3월 23일 토요일 14:00 - 18:00",
            location = null,
            eventName = "데모데이",
            isAttendancePointAwardedEvent = false
        )

        private val USER_INFO_SEUNGHYEON = AttendanceHistoryResponse.AttendanceUserInfoResponse(
            generation = 30,
            partName = "안드로이드",
            userName = "한승현",
            attendancePoint = 0.5
        )
        private val ATTENDANCE_SUMMARY_SEUNGHYEON = AttendanceHistoryResponse.AttendanceSummaryResponse(
            all = 11,
            normal = 9,
            late = 1,
            abnormal = 1
        )
        private val ATTENDANCE_LOG_SEUNGHYEON = listOf(
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "1차 세미나",
                date = "4월 3일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "지각",
                eventName = "2차 세미나",
                date = "4월 10일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "결석",
                eventName = "3차 세미나",
                date = "4월 17일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "4차 세미나",
                date = "4월 24일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "5차 세미나",
                date = "5월 1일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "6차 세미나",
                date = "5월 8일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "7차 세미나",
                date = "5월 15일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "8차 세미나",
                date = "5월 22일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "9차 세미나",
                date = "5월 29일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "10차 세미나",
                date = "6월 5일"
            ),
            AttendanceHistoryResponse.AttendanceLogResponse(
                attendanceState = "출석",
                eventName = "11차 세미나",
                date = "6월 12일"
            )
        )
        private val ATTENDANCE_HISTORY =
            AttendanceHistoryResponse(
                userInfo = USER_INFO_SEUNGHYEON,
                attendanceSummary = ATTENDANCE_SUMMARY_SEUNGHYEON,
                attendanceLog = ATTENDANCE_LOG_SEUNGHYEON
            )
    }
}