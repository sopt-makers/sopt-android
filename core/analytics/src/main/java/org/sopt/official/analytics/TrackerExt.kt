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
package org.sopt.official.analytics

fun Tracker.track(event: AnalyticsEvent, properties: Map<String, Any?> = emptyMap()) {
    track(type = event.type, name = event.eventName, properties = properties)
}

/**
 * view_type 프로퍼티를 포함해 이벤트를 전송하는 함수
 *
 * @param type 이벤트 타입
 * @param name 이벤트 이름
 * @param viewType 유저 상태 값
 * @param properties 추가 프로퍼티
 */
fun Tracker.trackViewType(
    type: EventType,
    name: String,
    viewType: String,
    properties: Map<String, Any?> = emptyMap(),
) {
    track(
        type = type,
        name = name,
        properties = mapOf(
            AnalyticsPropertyKey.VIEW_TYPE to viewType,
        ) + properties,
    )
}

/**
 * view_type 프로퍼티를 포함해 이벤트를 전송하는 함수
 *
 * @param event 이벤트 객체
 * @param viewType 유저 상태 값
 * @param properties 추가 프로퍼티
 */
fun Tracker.trackViewType(
    event: AnalyticsEvent,
    viewType: String,
    properties: Map<String, Any?> = emptyMap(),
) {
    trackViewType(
        type = event.type,
        name = event.eventName,
        viewType = viewType,
        properties = properties,
    )
}
