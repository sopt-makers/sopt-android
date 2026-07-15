package org.sopt.official.feature.soptlog

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class SoptlogAnalyticsEvent(
    override val type : EventType,
    override val eventName : String
):AnalyticsEvent {
    VIEW_SOPTLOG_MAIN(EventType.VIEW,"soptlog_main"),
}