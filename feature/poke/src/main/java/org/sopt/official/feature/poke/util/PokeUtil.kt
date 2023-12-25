package org.sopt.official.feature.poke.util

import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType

fun PokeUser.getPokeFriendRelationColor(): Int {
    return when (relationName) {
        PokeFriendType.NEW.name -> org.sopt.official.designsystem.R.color.mds_blue_900
        PokeFriendType.BEST_FRIEND.name -> org.sopt.official.designsystem.R.color.mds_information
        PokeFriendType.SOULMATE.name -> org.sopt.official.designsystem.R.color.mds_secondary
        else -> org.sopt.official.designsystem.R.color.transparent
    }
}