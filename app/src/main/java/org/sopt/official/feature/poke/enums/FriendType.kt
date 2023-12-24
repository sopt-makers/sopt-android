package org.sopt.official.feature.poke.enums

import org.sopt.official.R

enum class FriendType(
    val relationName: String,
    val borderResourceId: Int
) {
    NEW_FRIEND(
        relationName = "친한친구",
        borderResourceId =  R.drawable.oval_border_blue400
    ),
    BEST_FRIEND(relationName = "단짝친구",
        borderResourceId = R.drawable.oval_border_green400
    ),
    SOULMATE(relationName = "천생연분",
        borderResourceId = R.drawable.oval_border_orange400
    )
}