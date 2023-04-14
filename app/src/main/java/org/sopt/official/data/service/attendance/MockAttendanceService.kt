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

    override suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse> {
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
                    "startDate": "",
                    "endDate": "",
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
                    "startDate": "2023-04-06T14:13:51.588149",
                    "endDate": "2023-04-06T18:13:51.588149",
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
		            "startDate": "2023-04-06T14:13:51.588149",
		            "endDate": "2023-04-06T18:13:51.588149",
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
                    "startDate": "2023-04-06T14:13:51.588149",
                    "endDate": "2023-04-06T18:13:51.588149",
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
                    "id": 1,
                    "type": "NO_ATTENDANCE",
                    "location": "건국대학교 경영관",
                    "name": "1차 행사",
                    "startDate": "2023-04-06T14:13:51.588149",
                    "endDate": "2023-04-06T18:13:51.588149",
                    "message": "행사도 참여하고, 출석점수도 받고, 일석이조!",
                    "attendances": []
                }
            }
        """
        private val EVENT_NO_ATTENDANCE: BaseAttendanceResponse<SoptEventResponse> =
            Json.decodeFromString(EVENT_NO_ATTENDANCE_JSON_TEXT)

        private const val ATTENDANCE_HISTORY_JSON_TEXT = """
            {
                "success": true,
                "message": "전체 출석정보 조회 성공",
                "data": {
                    "part": "SERVER",
                    "generation": 32,
                    "name": "용택",
                    "score": 1.0,
                    "total": {
                        "attendance": 1,
                        "absent": 1,
                        "tardy": 1,
                        "participate": 1
                    }
                    "attendances": [
                        {
                            "attribute": "ETC",
                            "name": "솝커톤",
                            "status": "PARTICIPATE",
                            "date": "5월 16일"
                        },
                        {
                            "attribute": "SEMINAR",
                            "name": "서버 2차 세미나",
                            "status": "ATTENDANCE",
                            "date": "4월 14일"
                        },
                        {
                            "attribute": "SEMINAR",
                            "name": "서버 1차 세미나",
                            "status": "ABSENT",
                            "date": "4월 10일"
                        },
                        {
                            "attribute": "SEMINAR",
                            "name": "OT",
                            "status": "TARDY",
                            "date": "4월 2일"
                        }
                    ]
                }
            }
        """
        private val ATTENDANCE_HISTORY: BaseAttendanceResponse<AttendanceHistoryResponse> =
            Json.decodeFromString(ATTENDANCE_HISTORY_JSON_TEXT)
    }
}