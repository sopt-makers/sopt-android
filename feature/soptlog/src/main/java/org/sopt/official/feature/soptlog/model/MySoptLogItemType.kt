package org.sopt.official.feature.soptlog.model

import org.sopt.official.domain.soptlog.model.SoptLogInfo

internal enum class MySoptLogItemType(
    val title: String,
    val category: SoptLogCategory,
    val count: (SoptLogInfo) -> Int,
    val url: String = "",
    val hasHelpIcon: Boolean = false,
    val hasArrow: Boolean = true
) {
    // 솝탬프 로그
    COMPLETED_MISSION(title = "완료미션", category = SoptLogCategory.SOPTAMP, url = "soptamp", count = { it.soptampCount }),
    VIEW_COUNT(title = "조회수", category = SoptLogCategory.SOPTAMP, hasHelpIcon = true, hasArrow = false, count = { it.viewCount }),
    RECEIVED_CLAP(title = "받은 박수", category = SoptLogCategory.SOPTAMP, hasArrow = false, count = { it.myClapCount }),
    SENT_CLAP(title = "쳐준 박수", category = SoptLogCategory.SOPTAMP, hasArrow = false, count = { it.clapCount }),

    // 콕찌르기 로그
    TOTAL_POKE(title = "총 콕찌르기", category = SoptLogCategory.POKE, url = "home/poke", count = { it.pokeCount ?: 0 }),
    CLOSE_FRIEND(title = "친한 친구", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=new", count = { it.newFriendsPokeCount ?: 0 }),
    BEST_FRIEND(title = "단짝 친구", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=bestfriend", count = { it.bestFriendsPokeCount ?: 0 }),
    SOULMATE(title = "천생 연분", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=soulmate", count = { it.soulmatesPokeCount ?: 0 });
}