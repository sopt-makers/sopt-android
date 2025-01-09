package org.sopt.official

import org.junit.jupiter.api.Test
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance.NotYet.AttendanceSession.FIRST
import org.sopt.official.feature.attendance.model.MidtermAttendance.NotYet.AttendanceSession.SECOND

class FinalAttendanceTest {
    private lateinit var firstRoundAttendance: MidtermAttendance
    private lateinit var secondRoundAttendance: MidtermAttendance

    @Test
    fun `1차 또는 2차 출석 여부가 아직 결정되지 않은 경우에는 최종 출석이 아직 결정되지 않은 상태로 한다`() {
        firstRoundAttendance = MidtermAttendance.NotYet(FIRST)
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.NOT_YET)

        firstRoundAttendance = MidtermAttendance.NotYet(FIRST)
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.NOT_YET)

        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.NotYet(SECOND)
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.NOT_YET)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.NotYet(SECOND)
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.NOT_YET)
    }

    @Test
    fun `1차, 2차 출석 여부가 모두 출석일 경우 출석으로 한다`() {
        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.PRESENT)

        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.PRESENT)

        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.NotYet(SECOND)
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.PRESENT)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.PRESENT)

        firstRoundAttendance = MidtermAttendance.NotYet(FIRST)
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.PRESENT)
    }

    @Test
    fun `1차, 2차 출석 여부가 모두 결석일 경우 결석으로 한다`() {
        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.ABSENT)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.ABSENT)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.NotYet(SECOND)
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.ABSENT)

        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.ABSENT)

        firstRoundAttendance = MidtermAttendance.NotYet(FIRST)
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.ABSENT)
    }

    @Test
    fun `1차, 2차 출석 중 한 번은 출석하고 한 번은 결석한 경우 지각으로 한다`() {
        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.LATE)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) == FinalAttendance.LATE)

        firstRoundAttendance = MidtermAttendance.Present(attendanceAt = "14:00")
        secondRoundAttendance = MidtermAttendance.Present(attendanceAt = "16:00")
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.LATE)

        firstRoundAttendance = MidtermAttendance.Absent
        secondRoundAttendance = MidtermAttendance.Absent
        assert(FinalAttendance.calculateFinalAttendance(firstRoundAttendance, secondRoundAttendance) != FinalAttendance.LATE)
    }

}
