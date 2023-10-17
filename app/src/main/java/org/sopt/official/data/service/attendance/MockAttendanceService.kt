/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.service.attendance

import kotlinx.serialization.json.Json
import org.sopt.official.data.model.attendance.*

class MockAttendanceService : AttendanceService {
    override suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse> {
        return NOT_EVENT_DAY
    }

    override suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse> {
        return ATTENDANCE_HISTORY
    }

    override suspend fun getAttendanceRound(lectureId: Long): BaseAttendanceResponse<AttendanceRoundResponse> {
        return ATTENDANCE_ROUND_ONE
    }

    override suspend fun confirmAttendanceCode(param: RequestAttendanceCode): BaseAttendanceResponse<AttendanceCodeResponse> {
        return FAIL_ATTENDANCE_BEFORE
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
                    },
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

        private const val NO_SECTION_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 오늘 세션이 없습니다.",
              "data": null
            }
        """
        private val NO_SECTION: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(NO_SECTION_JSON_TEXT)

        private const val NO_ATTENDANCE_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 출석 시작 전입니다.",
              "data": null
            }
        """
        private val NO_ATTENDANCE: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(NO_ATTENDANCE_JSON_TEXT)

        private const val NO_TIME_FIRST_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 1차 출석 시작 전입니다.",
              "data": null
            }
        """
        private val NO_TIME_FIRST: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(NO_TIME_FIRST_JSON_TEXT)

        private const val NO_TIME_SECOND_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 2차 출석 시작 전입니다.",
              "data": null
            }
        """
        private val NO_TIME_SECOND: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(NO_TIME_SECOND_JSON_TEXT)

        private const val AFTER_TIME_FIRST_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 1차 출석이 이미 종료되었습니다.",
              "data": null
            }
        """
        private val AFTER_TIME_FIRST: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(AFTER_TIME_FIRST_JSON_TEXT)

        private const val AFTER_TIME_SECOND_JSON_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 2차 출석이 이미 종료되었습니다.",
              "data": null
            }
        """
        private val AFTER_TIME_SECOND: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(AFTER_TIME_SECOND_JSON_TEXT)

        private const val ATTENDANCE_ROUND_TWO_JSON_TEXT = """
            {
              "success": true,
              "message": "출석 차수 조회 성공",
              "data": {
                "id": 16,
                "round": 2
              }
            }
        """
        private val ATTENDANCE_ROUND_TWO: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(ATTENDANCE_ROUND_TWO_JSON_TEXT)

        private const val ATTENDANCE_ROUND_ONE_JSON_TEXT = """
            {
              "success": true,
              "message": "출석 차수 조회 성공",
              "data": {
                "id": 5,
                "round": 1
              }
            }
        """
        private val ATTENDANCE_ROUND_ONE: BaseAttendanceResponse<AttendanceRoundResponse> =
            Json.decodeFromString(ATTENDANCE_ROUND_ONE_JSON_TEXT)

        private const val SUCCESS_ATTENDNACE_TEXT = """
            {
              "success": true,
              "message": "출석 성공",
              "data": {
                "subLectureId": 17
              }
            }
        """
        private val SUCCESS_ATTENDANCE: BaseAttendanceResponse<AttendanceCodeResponse> =
            Json.decodeFromString(SUCCESS_ATTENDNACE_TEXT)

        private const val FAIL_ATTENDNACE_WRONG_CODE_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 코드가 일치하지 않아요!",
              "data": null
            }
        """
        private val FAIL_ATTENDNACE_WRONG_CODE: BaseAttendanceResponse<AttendanceCodeResponse> =
            Json.decodeFromString(FAIL_ATTENDNACE_WRONG_CODE_TEXT)

        private const val FAIL_ATTENDANCE_BEFORE_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 1차 출석 시작 전입니다",
              "data": null
            }
        """
        private val FAIL_ATTENDANCE_BEFORE: BaseAttendanceResponse<AttendanceCodeResponse> =
            Json.decodeFromString(FAIL_ATTENDANCE_BEFORE_TEXT)

        private const val FAIL_ATTENDANCE_AFTER_TIME_TEXT = """
            {
              "success": false,
              "message": "[LectureException] : 1차 출석 시작 전입니다",
              "data": null
            }
        """
        private val FAIL_ATTENDANCE_AFTER_TIME: BaseAttendanceResponse<AttendanceCodeResponse> =
            Json.decodeFromString(FAIL_ATTENDANCE_AFTER_TIME_TEXT)
    }
}
