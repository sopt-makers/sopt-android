package org.sopt.official.feature.home

import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker

fun trackClickEvent(tracker: Tracker, eventName: String, eventType: EventType = EventType.CLICK) {
    tracker.track(name = eventName, type = eventType)
}

fun isValidUrl(url: String): Boolean = url.startsWith("home/")
