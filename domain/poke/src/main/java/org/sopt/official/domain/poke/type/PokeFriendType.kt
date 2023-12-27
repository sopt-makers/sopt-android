package org.sopt.official.domain.poke.type

enum class PokeFriendType(
    val typeName: String,
    val readableName: String,
) {
    NEW(
        typeName = "new",
        readableName = "친한친구"
    ),
    BEST_FRIEND(
        typeName = "bestfriend",
        readableName = "단짝친구"
    ),
    SOULMATE(
        typeName = "soulmate",
        readableName = "천생연분"
    ),
}