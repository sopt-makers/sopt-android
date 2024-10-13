package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MissionStatusResponse(
    val id: Int,
    val title: String,
    val level: Int,
    val profileImage: List<String>?,
)
