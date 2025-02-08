package org.sopt.official.domain.schedule.model

data class Schedule(
    val date: String,
    val title: String,
    val type: String,
    val isRecentSchedule: Boolean,
)
