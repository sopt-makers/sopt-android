package org.sopt.official.analytics

interface AnalyticsEvent {
    val type: EventType
    val eventName: String
}
