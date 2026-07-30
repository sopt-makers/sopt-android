package org.sopt.official.feature.poke

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType

enum class PokeAnalyticsEvent(
    override val type: EventType,
    override val eventName: String,
) : AnalyticsEvent {
    VIEW_POKE_ONBOARDING(EventType.VIEW, "poke_onboarding"),
    VIEW_POKE_ONBOARDING_FRAGMENT(EventType.VIEW, "poke_onboarding_fragment"),
    VIEW_POKE_MAIN(EventType.VIEW, "poke_main"),
    VIEW_POKE_FRIEND(EventType.VIEW, "poke_friend"),
    VIEW_POKE_FRIEND_DETAIL(EventType.VIEW, "poke_friend_detail"),
    VIEW_POKE_ALARM_DETAIL(EventType.VIEW, "poke_alarm_detail"),
    CLICK_POKE_ICON(EventType.CLICK, "poke_icon"),
    CLICK_MEMBER_PROFILE(EventType.CLICK, "memberprofile"),
    CLICK_POKE_ALARM_DETAIL(EventType.CLICK, "poke_alarm_detail"),
    CLICK_QUIT_POKE(EventType.CLICK, "quit_poke"),
    CLICK_POKE_SEND_MESSAGE(EventType.CLICK, "poke_send_message"),
    CLICK_POKE_ANONYMITY(EventType.CLICK, "poke_anonymity"),
}

internal object PokeAnalyticsPropertyKey {
    const val CLICK_SOURCE = "poke_click_source"
    const val FRIEND_TYPE = "friend_type"
    const val MESSAGE_TYPE = "message_type"
    const val MESSAGE_ID = "message_id"
    const val IS_ANONYMOUS = "is_anonymous"
    const val VIEW_PROFILE = "view_profile"
}

internal enum class PokeClickSource(
    val value: String,
) {
    ONBOARDING("onboarding"),
    MAIN_RECOMMENDATION("main_recommendation"),
    MAIN_ALARM("main_alarm"),
    MAIN_FRIEND("main_friend"),
    ALARM_DETAIL("alarm_detail"),
    FRIEND_SUMMARY("friend_summary"),
    FRIEND_DETAIL("friend_detail"),
}

/**
 * [PokeFriendType]을 Amplitude의 `friend_type` 프로퍼티 값으로 변환하는 함수
 *
 * @return `new_friend`, `best_friend`, `soulmate` 중 하나
 */
internal fun PokeFriendType.toAnalyticsValue(): String = when (this) {
    PokeFriendType.NEW -> "new_friend"
    PokeFriendType.BEST_FRIEND -> "best_friend"
    PokeFriendType.SOULMATE -> "soulmate"
}

/**
 * [PokeMessageType]을 Amplitude의 `message_type` 프로퍼티 값으로 변환하는 함수
 *
 * @return `poke_someone`, `poke_friend` 중 하나
 */
internal fun PokeMessageType.toAnalyticsValue(): String = when (this) {
    PokeMessageType.POKE_SOMEONE -> "poke_someone"
    PokeMessageType.POKE_FRIEND -> "poke_friend"
}
