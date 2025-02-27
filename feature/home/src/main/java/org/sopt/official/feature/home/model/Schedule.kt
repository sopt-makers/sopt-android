package org.sopt.official.feature.home.model

import org.sopt.official.domain.home.model.ScheduleType

internal enum class Schedule(val titleKR: String) {
    EVENT("행사"), SEMINAR("세미나")
    ;

    companion object {
        fun from(type: ScheduleType): Schedule =
            Schedule.entries.find {
                it.titleKR == type.titleKR
            } ?: EVENT
    }
}
