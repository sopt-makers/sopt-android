package org.sopt.official.domain.entity.poke

data class PokeNotificationHistoryItem (
    val userId: Long,
    val profileImage: String,
    val name: String,
    val message: String,
    val generation: Int,
    val part: String,
    val pokeNum: Int,
    val mutual: List<String>,
    val isFirstMeet: Boolean,
    val isAlreadyPoke: Boolean
)