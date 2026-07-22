package org.sopt.official.stamp

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class SoptampAnalyticsEvent(
    override val type: EventType,
    override val eventName: String
) : AnalyticsEvent {
    VIEW_SOPTAMP(EventType.VIEW, "soptamp"),
    CLICK_UPDATE_CLAP(EventType.CLICK, "update_clap"),
    CLICK_GET_IMAGE_ZOOM(EventType.CLICK, "get_image_zoom"),
    CLICK_FEED_MISSION(EventType.CLICK, "feed_mission"),
    VIEW_ALLRANKING(EventType.VIEW, "allranking"),
    VIEW_PARTRANKING(EventType.VIEW, "partranking"),
    CLICK_ALLRANKING_MYRANKING(EventType.CLICK, "allranking_myranking"),
    CLICK_PARTRANKING_MYRANKING(EventType.CLICK, "partranking_myranking"),
    CLICK_CLAPPERLIST(EventType.CLICK, "clapperlist"),
}
