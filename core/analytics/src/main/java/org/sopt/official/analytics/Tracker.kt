package org.sopt.official.analytics

interface Tracker {
    fun track(type: EventType, name: String, properties: Map<String, Any?> = emptyMap())
    fun setNotificationStateToUserProperties(value: Boolean)
}
