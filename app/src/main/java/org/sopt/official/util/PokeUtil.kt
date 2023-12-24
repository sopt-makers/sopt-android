package org.sopt.official.util

import org.sopt.official.R
import org.sopt.official.feature.poke.enums.FriendType

object PokeUtil {
    fun setPokeIcon(isFirstMeet: Boolean, isAlreadyPoke: Boolean): Int {
        return when {
            isFirstMeet && isAlreadyPoke -> R.drawable.ic_eyes_gray500
            isFirstMeet -> R.drawable.ic_eyes_black
            isAlreadyPoke -> R.drawable.ic_poke_gray500
            else -> R.drawable.ic_poke_black
        }
    }

    fun convertRelationNameToBorderReSourceId(relationName: String): Int {
        return when (relationName) {
            FriendType.NEW_FRIEND.relationName -> FriendType.NEW_FRIEND.borderResourceId
            FriendType.BEST_FRIEND.relationName -> FriendType.BEST_FRIEND.borderResourceId
            FriendType.SOULMATE.relationName -> FriendType.SOULMATE.borderResourceId
            else -> { FriendType.NEW_FRIEND.borderResourceId }
        }
    }
}