package org.sopt.official.feature.setting.model

import androidx.annotation.DrawableRes

data class SettingItem(
    val title: String,
    @DrawableRes val rightIcon: Int = -1,
    val onClick: () -> Unit = {},
    val contentDescription: String = ""
) {
    fun setOnClick(onClick: () -> Unit) = copy(onClick = onClick)
}
