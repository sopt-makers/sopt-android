/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.setting.model

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.style.Gray50
import org.sopt.official.stamp.designsystem.style.Gray900

sealed interface SectionUiModel {
    @Immutable
    data class Option(
        val title: String,
        @DrawableRes val optionIconResId: Int = -1,
        val containerModifier: Modifier = Modifier
            .background(Gray50)
            .padding(horizontal = 16.dp),
        val textColor: Color = Gray900,
        val onPress: () -> Unit = {}
    ) : SectionUiModel

    @Immutable
    data class Header(
        val containerModifier: Modifier = Modifier
            .background(
                color = Gray50,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .padding(horizontal = 16.dp),
        val title: String
    ) : SectionUiModel

    @Immutable
    data object Spacer : SectionUiModel
}
