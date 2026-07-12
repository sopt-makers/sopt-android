package org.sopt.official.feature.mypage

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class MypageAnalyticsEvent(
    override val type : EventType,
    override val eventName : String
):AnalyticsEvent {
    VIEW_MYPAGE_MAIN(EventType.VIEW,"mapage_main"),
    CLICK_PROFILE_EDIT_BUTTON(EventType.CLICK,"profile_edit_button"),
    CLICK_MYPAGE_SOPTLOG(EventType.CLICK,"mypage_soptlog"),
    CLICK_MYPAGE_FEEDBACK(EventType.CLICK,"mypage_feedback"),
    CLICK_MYPAGE_NOTIFICATION(EventType.CLICK,"mypage_notification"),
    CLICK_MYPAGE_EDIT_STATUSMESSAGE(EventType.CLICK,"mypage_edit_statusmessage"),
    CLICK_DONE_EDIT_STATUSMESSAGE(EventType.CLICK,"done_edit_statusmessage"),
    CLICK_MYPAGE_RESET_STAMP(EventType.CLICK,"mypage_reset_stamp"),
    CLICK_DONE_LOGOUT(EventType.CLICK,"done_logout"),
    CLICK_MYPAGE_QUIT(EventType.CLICK,"mypage_quit"),
}