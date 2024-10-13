package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class InCompleteMissionResponse(
    val id: Int,
    val title: String,
    val level: Int,
    val profileImage: List<String>?,
)
