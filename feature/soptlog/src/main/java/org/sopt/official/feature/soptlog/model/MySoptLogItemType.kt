package org.sopt.official.feature.soptlog.model

import org.sopt.official.domain.soptlog.model.SoptLogInfo

internal enum class MySoptLogItemType(
    val title: String,
    val count: (SoptLogInfo) -> Int,
    val hasHelpIcon: Boolean = false,
    val hasArrow: Boolean = true
) {
    // 솝탬프 로그
    COMPLETED_MISSION("완료미션", count = { it.soptampCount }),
    VIEW_COUNT("조회수", hasHelpIcon = true, hasArrow = false, count = { it.viewCount }),
    RECEIVED_CLAP("받은 박수", hasArrow = false, count = { it.myClapCount }),
    SENT_CLAP("쳐준 박수", hasArrow = false, count = { it.clapCount }),

    // 콕찌르기 로그
    TOTAL_POKE("총 콕찌르기", count = { it.pokeCount ?: 0 }),
    CLOSE_FRIEND("친한 친구", count = { it.newFriendsPokeCount ?: 0 }),
    BEST_FRIEND("단짝 친구", count = { it.bestFriendsPokeCount ?: 0 }),
    SOULMATE("천생 연분", count = { it.soulmatesPokeCount ?: 0 });
}
