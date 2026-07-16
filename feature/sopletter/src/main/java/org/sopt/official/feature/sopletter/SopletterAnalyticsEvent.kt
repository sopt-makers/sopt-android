package org.sopt.official.feature.sopletter

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class SopletterAnalyticsEvent(
    override val type: EventType,
    override val eventName: String,
) : AnalyticsEvent {
    VIEW_SOPTLETTER_ONBOARDING(
        type = EventType.VIEW,
        eventName = "soptletter_onboarding",
    ),
    VIEW_SOPTLETTER_NICKNAME(
        type = EventType.VIEW,
        eventName = "soptletter_nickname",
    ),
    VIEW_SOPTLETTER_MAIN(
        type = EventType.VIEW,
        eventName = "soptletter_main",
    ),
    CLICK_SOPTLETTER_START_BUTTON(
        type = EventType.CLICK,
        eventName = "soptletter_start_button",
    ),
    CLICK_WRITE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "write_soptletter",
    ),
    CLICK_DONE_WRITE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_write_soptletter",
    ),
    CLICK_SOPTLETTER_DETAIL(
        type = EventType.CLICK,
        eventName = "soptletter_detail",
    ),
    CLICK_EDIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "edit_soptletter",
    ),
    CLICK_DONE_EDIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_edit_soptletter",
    ),
    CLICK_SOPTLETTER_LIKE_BUTTON(
        type = EventType.CLICK,
        eventName = "soptletter_like_button",
    ),
    CLICK_DELETE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "delete_soptletter",
    ),
    CLICK_EXPORT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "export_soptletter",
    ),
    CLICK_DONE_EXPORT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_export_soptletter",
    ),
    CLICK_QUIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "quit_soptletter",
    ),
}
