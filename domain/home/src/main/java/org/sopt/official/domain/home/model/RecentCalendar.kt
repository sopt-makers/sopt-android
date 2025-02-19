package org.sopt.official.domain.home.model

data class RecentCalendar(
    val date: String = "",
    val type: String = "",
    val title: String = "",
) {
    val scheduleType: ScheduleType = ScheduleType.from(type)
}

enum class ScheduleType(val titleKR: String) {
    EVENT("행사"), SEMINAR("세미나")
    ;

    companion object {
        fun from(type: String): ScheduleType =
            ScheduleType.entries.find {
                it.name == type
            } ?: EVENT
    }
}
