/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.notification

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class NotificationAnalyticsEvent(
    override val type: EventType,
    override val eventName: String,
) : AnalyticsEvent {
    VIEW_NOTIFICATION_LIST(EventType.VIEW, "notification_list"),
    CLICK_LINK_BUTTON(EventType.CLICK, "link_button"),
    CLICK_ALLREAD_BUTTON(EventType.CLICK, "allread_button"),
    CLICK_NOTIFICATION_ITEM(EventType.CLICK, "notification_item"),
    RECEIVED_PUSH(EventType.RECEIVED, "push"),
    CLICK_PUSH(EventType.CLICK, "push"),
}

object NotificationAnalyticsPropertyKey {
    const val NOTIFICATION_ID = "notification_id"
    const val NOTIFICATION_LINK_TYPE = "notification_link_type"
    const val NOTIFICATION_LAUNCH_TYPE = "notification_launch_type"
}

enum class NotificationLinkType(
    val value: String,
) {
    WEB("web"),
    DEEP_LINK("deep_link"),
    NONE("none"),
}

enum class NotificationLaunchType(
    val value: String,
) {
    COLD_START("cold_start"),
    WARM_START("warm_start"),
}

/**
 * 알림 링크를 Amplitude의 `notification_link_type` 프로퍼티 값으로 변환하는 함수
 *
 * @return 웹 URL은 `web`, 앱 딥링크는 `deep_link`, 링크가 없으면 `none`
 */
fun String?.toNotificationLinkType(): NotificationLinkType = when {
    isNullOrBlank() -> NotificationLinkType.NONE
    startsWith("http://") || startsWith("https://") -> NotificationLinkType.WEB
    else -> NotificationLinkType.DEEP_LINK
}
