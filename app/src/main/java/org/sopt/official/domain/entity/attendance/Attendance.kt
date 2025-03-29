package org.sopt.official.domain.entity.attendance

import java.time.LocalDateTime

data class Attendance(
    val lectureId: Long,
    val user: User,
    val attendanceDayType: AttendanceDayType,
) {
    data class User(
        val name: String?,
        val generation: Int?,
        val part: Part?,
        val attendanceScore: Number,
        val attendanceCount: AttendanceCount,
        val attendanceHistory: List<AttendanceLog>
    ) {
        enum class Part(val partName: String) {
            PLAN("기획"),
            DESIGN("디자인"),
            ANDROID("안드로이드"),
            IOS("iOS"),
            WEB("웹"),
            SERVER("서버");

            companion object {
                fun of(partName: String?): Part? {
                    if (partName == null) return null
                    return entries.find { part: Part -> part.partName == partName }
                }
            }
        }

        data class AttendanceCount(
            /** 출석 전체 횟수 */
            val attendanceCount: Int,
            /** 지각 전체 횟수 */
            val lateCount: Int,
            /** 결석 전체 횟수 */
            val absenceCount: Int,
        ) {
            /** 전체 횟수 */
            val totalCount: Int
                get() = attendanceCount + lateCount + absenceCount
        }

        data class AttendanceLog(
            val sessionName: String,
            val date: String,
            val attendanceState: AttendanceState
        ) {
            enum class AttendanceState {
                /** 참여(출석 체크 X)*/
                PARTICIPATE,

                /** 출석 */
                ATTENDANCE,

                /** 지각 */
                TARDY,

                /** 결석 */
                ABSENT
            }
        }
    }

    sealed interface AttendanceDayType {

        /** 일정이 없는 날 */
        data object NoSession : AttendanceDayType

        /** 일정이 있고, 출석 체크가 있는 날 */
        data class HasAttendance(
            val session: Session,
            val firstRoundAttendance: RoundAttendance,
            val secondRoundAttendance: RoundAttendance
        ) : AttendanceDayType {
            /** n차 출석에 관한 정보 */
            data class RoundAttendance(
                val state: RoundAttendanceState,
                val attendedAt: LocalDateTime?
            ) {
                /** n차 출석 상태 */
                enum class RoundAttendanceState {
                    ABSENT, ATTENDANCE, NOT_YET,
                }
            }
        }

        /** 일정이 있고, 출석 체크가 없는 날 */
        data class NoAttendance(val session: Session) : AttendanceDayType
    }

    /** 솝트의 세션에 관한 정보
     * @property name 세션 이름 (OT, 1차 세미나, 솝커톤 등)
     * @property location 세션 장소, 정해진 장소가 없을 경우(온라인) null
     * @property startAt 세션 시작 시각
     * @property endAt 세션 종료 시각
     * */
    data class Session(
        val name: String,
        val location: String?,
        val startAt: LocalDateTime,
        val endAt: LocalDateTime,
    )

    companion object {
        /** 세션이 올바르게 생성되지 않은 경우 */
        const val UNKNOWN_SESSION_ID: Long = -1L

        /** 출석해야 할 세션이 존재하지 않는 경우 */
        const val NO_SESSION_ID: Long = 0L
    }
}