package org.sopt.official.data.schedule.repository

import com.sopt.official.domain.schedule.model.Schedule
import com.sopt.official.domain.schedule.repository.ScheduleRepository
import org.sopt.official.data.schedule.api.ScheduleApi
import org.sopt.official.data.schedule.mapper.toDomain
import javax.inject.Inject

class DefaultScheduleRepository @Inject constructor(
    private val scheduleApi: ScheduleApi,
) : ScheduleRepository {
    override suspend fun getScheduleList(): Result<List<Schedule>> = runCatching {
        scheduleApi.getSchedule().map { it.toDomain() }
    }
}
