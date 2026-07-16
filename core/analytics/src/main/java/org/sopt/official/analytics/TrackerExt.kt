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
