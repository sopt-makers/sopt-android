package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.SoptEventResponse

class MockAttendanceService : AttendanceService {
    override suspend fun getSoptEvent(): SoptEventResponse {
        return ATTENDANCE_POINT_AWARDED_EVENT_WITH_LOCATION
    }

    companion object {
        private val ATTENDANCE_POINT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
            date = "3월 23일 토요일 14:00 - 18:00",
            location = "건국대학교 꽥꽥오리관",
            eventName = "2차 세미나",
            isAttendancePointAwardedEvent = true
        )
        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
            date = "3월 23일 토요일 14:00 - 18:00",
            location = "건국대학교 꽥꽥오리관",
            eventName = "데모데이",
            isAttendancePointAwardedEvent = false
        )
        private val ATTENDANCE_POINT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
            date = "3월 23일 토요일 14:00 - 18:00",
            location = null,
            eventName = "2차 세미나",
            isAttendancePointAwardedEvent = true
        )
        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
            date = "3월 23일 토요일 14:00 - 18:00",
            location = null,
            eventName = "데모데이",
            isAttendancePointAwardedEvent = false
        )
    }
}