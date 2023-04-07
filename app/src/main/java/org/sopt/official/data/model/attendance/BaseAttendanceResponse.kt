package org.sopt.official.data.model.attendance

import kotlinx.serialization.Serializable

@Serializable
data class BaseAttendanceResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
