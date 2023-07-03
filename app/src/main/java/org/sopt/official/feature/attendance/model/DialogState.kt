package org.sopt.official.feature.attendance.model

sealed class DialogState {
    object Show : DialogState()
    object Close : DialogState()
    object Failure : DialogState()
}
