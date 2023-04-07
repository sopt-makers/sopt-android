package org.sopt.official.data.service.attendance

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.BaseAttendanceResponse
import org.sopt.official.data.model.attendance.SoptEventResponse

class MockAttendanceService : AttendanceService {
    override suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse> {
        return EVENT_NO_ATTENDANCE
    }

    override suspend fun getAttendanceHistory(): AttendanceHistoryResponse {
        return ATTENDANCE_HISTORY
    }

    companion object {
        private const val NOT_EVENT_DAY_JSON_TEXT = """
            {
                "success": true,
                "message": "세미나가 없는 날입니다",
                "data": {
                    "type": "NO_SESSION",
                    "location": "",
                    "name": "",
                    "startAt": null,
                    "endAt": null,
                    "attendances": []
                }
            }
        """
        private val NOT_EVENT_DAY: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(NOT_EVENT_DAY_JSON_TEXT)
        private const val SEMINAR_EVENT_BEFORE_START_JSON_TEXT = """
            {
                "success": true,
                "message": "세미나 조회 성공",
                "data": { 
                    "type": "HAS_ATTENDANCE",
                    "location": "건국대학교 경영관",
                    "name": "2차 세미나",
                    "startAt": "2023-04-06T14:13:51.588149",
                    "endAt": "2023-04-06T18:13:51.588149",
                    "attendances": [
                        {
                            "status": "ABSENT"
                        },
                        {
                            "status": "ABSENT"
                        }
                    ]
                }
            }
        """
        private val SEMINAR_EVENT_BEFORE_START: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(SEMINAR_EVENT_BEFORE_START_JSON_TEXT)
        private const val SEMINAR_EVENT_AFTER_START_BEFORE_END_JSON_TEXT = """
            {
                "success": true,
                "message": "세미나 조회 성공",
                "data": {
                    "type": "HAS_ATTENDANCE",
		            "location": "건국대학교 경영관",
		            "name": "2차 세미나",
		            "startAt": "2023-04-06T14:13:51.588149",
		            "endAt": "2023-04-06T18:13:51.588149",
		            "attendances": [
		                {
                            "status": "ATTENDANCE",
                            "attendedAt": "2023-04-06T14:13:51.588149"
                        },
                        {
                            "status": "ABSENT",
                            "attendedAt": "2023-04-06T16:12:04"
                        }
		            ]
	            }
            }
        """
        private val SEMINAR_EVENT_AFTER_START_BEFORE_END: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(SEMINAR_EVENT_AFTER_START_BEFORE_END_JSON_TEXT)
        private const val SEMINAR_EVENT_AFTER_END_JSON_TEXT = """
            {
                "success": true,
                "message": "세미나 조회 성공",
                "data": {
                    "type": "HAS_ATTENDANCE",
                    "location": "건국대학교 경영관",
                    "name": "2차 세미나",
                    "startAt": "2023-04-06T14:13:51.588149",
                    "endAt": "2023-04-06T18:13:51.588149",
                    "attendances": [
                        {
                            "status": "ATTENDANCE",
                            "attendedAt": "2023-04-06T14:13:51.588149"
                        },
                        {
                            "status": "ATTENDANCE",
                            "attendedAt": "2023-04-06T16:12:04"
                        }
                    ]
                }
            }
        """
        private val SEMINAR_EVENT_AFTER_END: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(SEMINAR_EVENT_AFTER_END_JSON_TEXT)
        private const val EVENT_NO_ATTENDANCE_JSON_TEXT = """
            {
            "success": true,
            "message": "세미나 조회 성공",
            "data": {
                    "type": "NO_ATTENDANCE",
                    "location": "건국대학교 경영관",
                    "name": "1차 행사",
                    "startAt": "2023-04-06T14:13:51.588149",
                    "endAt": "2023-04-06T18:13:51.588149",
                    "attendances": []
                }
            }
        """
        private val EVENT_NO_ATTENDANCE: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(EVENT_NO_ATTENDANCE_JSON_TEXT)


//        private val NOT_EVENT_DAY = SoptEventResponse(isEventDay = false)
//        private val ATTENDANCE_POINT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
//            isEventDay = true,
//            date = "3월 23일 토요일 14:00 - 18:00",
//            location = "건국대학교 꽥꽥오리관",
//            eventName = "2차 세미나",
//            isAttendancePointAwardedEvent = true
//        )
//        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITH_LOCATION = SoptEventResponse(
//            isEventDay = true,
//            date = "3월 23일 토요일 14:00 - 18:00",
//            location = "건국대학교 꽥꽥오리관",
//            eventName = "데모데이",
//            isAttendancePointAwardedEvent = false
//        )
//        private val ATTENDANCE_POINT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
//            isEventDay = true,
//            date = "3월 23일 토요일 14:00 - 18:00",
//            location = null,
//            eventName = "2차 세미나",
//            isAttendancePointAwardedEvent = true
//        )
//        private val ATTENDANCE_POINT_NOT_AWARDED_EVENT_WITHOUT_LOCATION = SoptEventResponse(
//            isEventDay = true,
//            date = "3월 23일 토요일 14:00 - 18:00",
//            location = null,
//            eventName = "데모데이",
//            isAttendancePointAwardedEvent = false
//        )

        private
        val USER_INFO_SEUNGHYEON = AttendanceHistoryResponse.AttendanceUserInfoResponse(
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