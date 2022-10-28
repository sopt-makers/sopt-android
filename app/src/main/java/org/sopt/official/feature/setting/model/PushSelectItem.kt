package org.sopt.official.feature.setting.model

import org.sopt.official.domain.entity.Part

data class PushSelectItem(
    val part: Part,
    val value: Boolean = false,
    val onCheckedChange: (Boolean) -> Unit = {}
)
