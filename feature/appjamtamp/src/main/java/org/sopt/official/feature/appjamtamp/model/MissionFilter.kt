package org.sopt.official.feature.appjamtamp.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class MissionFilter(
    val text: String,
    val isCompleted: Boolean?
) {
    ALL("전체 미션", null),
    COMPLETED("완료 미션", true),
    UNCOMPLETED("미완료 미션", false),
    APPJAM("앱잼 미션", null);

    companion object {
        fun getTitleOfMissionsList(): ImmutableList<String> = MissionFilter.entries.map { it.text }.toImmutableList()
        fun findFilterByText(text: String): MissionFilter =
            entries.find { it.text == text } ?: ALL
    }
}
