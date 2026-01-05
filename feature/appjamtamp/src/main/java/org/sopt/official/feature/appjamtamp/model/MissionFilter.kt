package org.sopt.official.feature.appjamtamp.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.domain.soptamp.model.MissionsFilter

enum class MissionFilter (
    val text : String,
    val isCompleted : Boolean?
) {
    ALL("앱잼 미션", null),
    COMPLETED("완료 미션", true),
    UNCOMPLETED("미완료 미션", false);

    companion object {
        fun getTitleOfMissionsList(): ImmutableList<String> = MissionsFilter.entries.map { it.title }.toImmutableList()
        fun findFilterByText(text : String) : MissionFilter =
            entries.find { it.text == text } ?: ALL
    }
}