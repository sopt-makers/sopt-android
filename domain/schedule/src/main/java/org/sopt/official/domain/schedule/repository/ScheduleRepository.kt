package org.sopt.official.domain.schedule.repository

import org.sopt.official.domain.schedule.model.Schedule

interface ScheduleRepository {
    suspend fun getScheduleList(): Result<List<Schedule>>
}
