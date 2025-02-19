package org.sopt.official.data.schedule.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    @SerialName("date")
    val date: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("isRecentSchedule")
    val isRecentSchedule: Boolean,
)
