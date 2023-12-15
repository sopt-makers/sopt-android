package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeMeResponse(
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
    @SerialName("activities")
    val activities: List<PokeMeActivity>,
    @SerialName("pockNum")
    val pockNum: Int,
    @SerialName("mutual")
    val mutual: List<String>,
    @SerialName("isFirstMeet")
    val isFirstMeet: Boolean,
    @SerialName("isAlreadyPoke")
    val isAlreadyPoke: Boolean
) {
    @Serializable
    data class PokeMeActivity(
        @SerialName("generation")
        val generation: Int,
        @SerialName("part")
        val part: String
    )
}
