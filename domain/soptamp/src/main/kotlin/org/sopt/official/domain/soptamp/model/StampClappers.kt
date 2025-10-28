package org.sopt.official.domain.soptamp.model

data class StampClappers(
    val clappers: List<StampClapUser>
) {
    data class StampClapUser(
        val nickname: String,
        val profileImage: String,
        val profileMessage: String,
        val clapCount: Int
    )
}


