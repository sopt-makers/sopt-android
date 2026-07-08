package org.sopt.official.analytics

fun Tracker.track(event: AnalyticsEvent, properties: Map<String, Any?> = emptyMap()) {
    track(type = event.type, name = event.eventName, properties = properties)
}
