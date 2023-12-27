package org.sopt.official.feature.poke.poke_user_recycler_view

import org.sopt.official.domain.poke.entity.PokeUser

interface PokeUserListClickListener {
    fun onClickProfileImage(playgroundId: Int)
    fun onClickPokeButton(user: PokeUser)
}