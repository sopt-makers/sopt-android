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
package org.sopt.official.feature.sopletter

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class SopletterAnalyticsEvent(
    override val type: EventType,
    override val eventName: String,
) : AnalyticsEvent {
    VIEW_SOPTLETTER_ONBOARDING(
        type = EventType.VIEW,
        eventName = "soptletter_onboarding",
    ),
    VIEW_SOPTLETTER_NICKNAME(
        type = EventType.VIEW,
        eventName = "soptletter_nickname",
    ),
    VIEW_SOPTLETTER_MAIN(
        type = EventType.VIEW,
        eventName = "soptletter_main",
    ),
    CLICK_SOPTLETTER_START_BUTTON(
        type = EventType.CLICK,
        eventName = "soptletter_start_button",
    ),
    CLICK_WRITE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "write_soptletter",
    ),
    CLICK_DONE_WRITE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_write_soptletter",
    ),
    CLICK_SOPTLETTER_DETAIL(
        type = EventType.CLICK,
        eventName = "soptletter_detail",
    ),
    CLICK_EDIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "edit_soptletter",
    ),
    CLICK_DONE_EDIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_edit_soptletter",
    ),
    CLICK_SOPTLETTER_LIKE_BUTTON(
        type = EventType.CLICK,
        eventName = "soptletter_like_button",
    ),
    CLICK_DELETE_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "delete_soptletter",
    ),
    CLICK_EXPORT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "export_soptletter",
    ),
    CLICK_DONE_EXPORT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "done_export_soptletter",
    ),
    CLICK_QUIT_SOPTLETTER(
        type = EventType.CLICK,
        eventName = "quit_soptletter",
    ),
}
