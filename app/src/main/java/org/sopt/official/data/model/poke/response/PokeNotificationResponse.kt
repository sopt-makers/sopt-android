package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeNotificationResponse (
    @SerialName("history")
    val history: List<PokeNotification>,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("pageNum")
    val pageNum: Int
) {
    @Serializable
    data class PokeNotification(
        @SerialName("userId")
        val userId: Long,
        @SerialName("playgroundId")
        val playgroundId: Long,
        @SerialName("profileImage")
        val profileImage: String,
        @SerialName("name")
        val name: String,
        @SerialName("message")
        val message: String,
        @SerialName("generation")
        val generation: Int,
        @SerialName("part")
        val part: String,
        @SerialName("pokeNum")
        val pokeNum: Int,
        @SerialName("relationName")
        val relationName: String,
        @SerialName("mutual")
        val mutual: List<String>,
        @SerialName("isFirstMeet")
        val isFirstMeet: Boolean,
        @SerialName("isAlreadyPoke")
        val isAlreadyPoke: Boolean
    )
}