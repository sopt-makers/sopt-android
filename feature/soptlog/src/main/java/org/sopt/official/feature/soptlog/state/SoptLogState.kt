package org.sopt.official.feature.soptlog.state

import org.sopt.official.domain.soptlog.model.SoptLogInfo

data class SoptLogState(
    val soptLogInfo: SoptLogInfo = SoptLogInfo(
        isActive = false,
        soptampCount = 0,
        viewCount = 0,
        myClapCount = 0,
        clapCount = 0,
        pokeCount = 0,
        newFriendsPokeCount = 0,
        bestFriendsPokeCount = 0,
        soulmatesPokeCount = 0
    ),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
