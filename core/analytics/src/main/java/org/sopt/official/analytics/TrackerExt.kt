package org.sopt.official.analytics

fun Tracker.track(event: AnalyticsEvent, properties: Map<String, Any?> = emptyMap()) {
    track(type = event.type, name = event.eventName, properties = properties)
}

fun Tracker.trackViewType(
    event: AnalyticsEvent,
    viewType: String,
    properties: Map<String, Any?> = emptyMap(),
) {
    track(
        event = event,
        properties = mapOf(
            AnalyticsPropertyKey.VIEW_TYPE to viewType,
        ) + properties,
    )
}
