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
package org.sopt.official.feature.home

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class HomeAnalyticsEvent(
    override val type: EventType,
    override val eventName: String
) : AnalyticsEvent {
    VIEW_APP_HOME(EventType.VIEW, "apphome"),
    CLICK_ALARM(EventType.CLICK, "alarm"),
    CLICK_ATTENDANCE(EventType.CLICK, "attendance"),
    CLICK_ALL_CALENDAR(EventType.CLICK, "all_calendar"),
    CLICK_PLAYGROUND_MEMBER(EventType.CLICK, "playground_member"),
    CLICK_PLAYGROUND_GROUP(EventType.CLICK, "playground_group"),
    CLICK_PLAYGROUND_PROJECT(EventType.CLICK, "playground_project"),
    CLICK_PLAYGROUND_COFFEE_CHAT(EventType.CLICK, "playground_coffee_chat"),
    CLICK_PLAYGROUND_COMMUNITY(EventType.CLICK, "playground_community"),
    CLICK_TODAYSOPTMADI(EventType.CLICK, "todaysoptmadi"),
    CLICK_SOPTLETTER_MENU(EventType.CLICK, "soptletter_menu"),
    CLICK_HOTBOARD(EventType.CLICK, "hotboard"),
    CLICK_HOMEPAGE(EventType.CLICK, "homepage"),
    CLICK_REVIEW(EventType.CLICK, "review"),
    CLICK_FAQ(EventType.CLICK, "faq"),
    CLICK_YOUTUBE(EventType.CLICK, "youtube"),
    CLICK_INSTAGRAM(EventType.CLICK, "instagram"),
    CLICK_SURVEY_BUTTON(EventType.CLICK, "survey_button"),
    CLICK_TOAST_BUTTON(EventType.CLICK, "toast_button")
}
