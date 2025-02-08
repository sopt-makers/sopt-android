package org.sopt.official.data.schedule.mapper

import com.sopt.official.domain.schedule.model.Schedule
import org.sopt.official.data.schedule.dto.ScheduleResponse

fun ScheduleResponse.toDomain() = Schedule(
    date = date,
    title = title,
    type = type,
    isRecentSchedule = isRecentSchedule
)
