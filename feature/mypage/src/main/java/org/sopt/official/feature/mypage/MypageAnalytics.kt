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
package org.sopt.official.feature.mypage

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType

enum class MypageAnalyticsEvent(
    override val type : EventType,
    override val eventName : String
):AnalyticsEvent {
    VIEW_MYPAGE_MAIN(EventType.VIEW,"mypage_main"),
    CLICK_PROFILE_EDIT_BUTTON(EventType.CLICK,"profile_edit_button"),
    CLICK_MYPAGE_SOPTLOG(EventType.CLICK,"mypage_soptlog"),
    CLICK_MYPAGE_FEEDBACK(EventType.CLICK,"mypage_feedback"),
    CLICK_MYPAGE_NOTIFICATION(EventType.CLICK,"mypage_notification"),
    CLICK_MYPAGE_EDIT_STATUSMESSAGE(EventType.CLICK,"mypage_edit_statusmessage"),
    CLICK_DONE_EDIT_STATUSMESSAGE(EventType.CLICK,"done_edit_statusmessage"),
    CLICK_MYPAGE_RESET_STAMP(EventType.CLICK,"mypage_reset_stamp"),
    CLICK_DONE_LOGOUT(EventType.CLICK,"done_logout"),
    CLICK_MYPAGE_QUIT(EventType.CLICK,"mypage_quit"),
}
