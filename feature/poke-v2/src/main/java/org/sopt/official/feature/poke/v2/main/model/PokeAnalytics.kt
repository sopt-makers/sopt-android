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
package org.sopt.official.feature.poke.v2.main.model

import org.sopt.official.analytics.AnalyticsEvent
import org.sopt.official.analytics.EventType
import org.sopt.official.domain.poke.type.PokeMessageType

enum class PokeAnalyticsEvent(
    override val type: EventType,
    override val eventName: String,
) : AnalyticsEvent {
    VIEW_POKE_ONBOARDING(EventType.VIEW, "poke_onboarding"),
    CLICK_MEMBER_PROFILE(EventType.CLICK, "memberprofile"),
    CLICK_POKE_ICON(EventType.CLICK, "poke_icon"),
    CLICK_POKE_SEND_MESSAGE(EventType.CLICK, "poke_send_message"),
    CLICK_POKE_ANONYMITY(EventType.CLICK, "poke_anonymity"),
}

internal object PokeAnalyticsPropertyKey {
    const val CLICK_SOURCE = "poke_click_source"
    const val MESSAGE_TYPE = "message_type"
    const val MESSAGE_ID = "message_id"
    const val IS_ANONYMOUS = "is_anonymous"
    const val VIEW_PROFILE = "view_profile"
}

internal enum class PokeClickSource(
    val value: String,
) {
    ONBOARDING("onboarding"),
}

internal fun PokeMessageType.toAnalyticsValue(): String = when (this) {
    PokeMessageType.POKE_SOMEONE -> "poke_someone"
    PokeMessageType.POKE_FRIEND -> "poke_friend"
}
