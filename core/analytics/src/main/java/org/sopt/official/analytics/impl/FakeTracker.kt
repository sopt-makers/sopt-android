package org.sopt.official.analytics.impl

import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker

object FakeTracker : Tracker {
    override fun track(type: EventType, name: String, properties: Map<String, Any?>) = Unit
    override fun setNotificationStateToUserProperties(value: Boolean) = Unit
}
