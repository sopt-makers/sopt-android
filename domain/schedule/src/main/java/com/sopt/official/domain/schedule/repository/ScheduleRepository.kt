package com.sopt.official.domain.schedule.repository

import com.sopt.official.domain.schedule.model.Schedule

interface ScheduleRepository {
    suspend fun getScheduleList(): Result<List<Schedule>>
}
