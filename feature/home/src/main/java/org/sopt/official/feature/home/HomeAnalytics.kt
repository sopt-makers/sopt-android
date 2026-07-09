package org.sopt.official.feature.home

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class HomeAnalyticsEvent(
    override val type: EventType,
    override val eventName: String
) : AnalyticsEvent {
    VIEW_APP_HOME(EventType.VIEW, "apphome"),
    CLICK_ALARM(EventType.CLICK, "alarm"),
    CLICK_ATTENDANCE(EventType.CLICK, "attendance"),
    CLICK_ALL_CALENDAR(EventType.CLICK, "all_calendar"),
    CLICK_PLAYGROUND_MEMBER(EventType.CLICK, "playground_member"),
    CLICK_PLAYGROUND_GROUP(EventType.CLICK, "playground_group"),
    CLICK_PLAYGROUND_PROJECT(EventType.CLICK, "playground_project"),
    CLICK_PLAYGROUND_COFFEE_CHAT(EventType.CLICK, "playground_coffee_chat"),
    CLICK_PLAYGROUND_COMMUNITY(EventType.CLICK, "playground_community"),
    CLICK_TODAYSOPTMADI(EventType.CLICK, "todaysoptmadi"),
    CLICK_SOPTLETTER_MENU(EventType.CLICK, "soptletter_menu"),
    CLICK_HOTBOARD(EventType.CLICK, "hotboard"),
    CLICK_HOMEPAGE(EventType.CLICK, "homepage"),
    CLICK_REVIEW(EventType.CLICK, "review"),
    CLICK_FAQ(EventType.CLICK, "faq"),
    CLICK_YOUTUBE(EventType.CLICK, "youtube"),
    CLICK_INSTAGRAM(EventType.CLICK, "instagram"),
    CLICK_SURVEY_BUTTON(EventType.CLICK, "survey_button"),
    CLICK_TOAST_BUTTON(EventType.CLICK, "toast_button")
}
