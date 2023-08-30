package org.sopt.official.feature.attendance.model

sealed class DialogState {
    data object Show : DialogState()
    data object Close : DialogState()
    data object Failure : DialogState()
}
