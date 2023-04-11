package org.sopt.official.domain.entity.attendance

enum class AttendanceStatus(val statusKorean: String) {
    ATTENDANCE("출석"), ABSENT("결석"), TARDY("지각"), PARTICIPATE("참여");
}