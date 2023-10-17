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
package org.sopt.official.stamp.designsystem.component.toolbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.util.MultiFormFactorPreviews

enum class ToolbarIconType(
    @DrawableRes private val resId: Int = -1
) {
    NONE,
    WRITE(R.drawable.ic_write),
    DELETE(R.drawable.ic_delete);

    companion object {
        @Composable
        fun getResourceFrom(type: ToolbarIconType) = painterResource(
            entries.find { it.name == type.name }?.resId
                ?: throw IllegalArgumentException("There's no icon in this class. Icon: ${type.name}")
        )
    }
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    iconOption: ToolbarIconType = ToolbarIconType.NONE,
    onBack: (() -> Unit)? = null,
    onPressIcon: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back Button",
                    modifier = Modifier
                        .noRippleClickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                )
            }
            title?.invoke()
        }

        if (iconOption != ToolbarIconType.NONE) {
            Image(
                painter = ToolbarIconType.getResourceFrom(iconOption),
                contentDescription = "Option Menu Icon",
                modifier = Modifier.noRippleClickable(onClick = onPressIcon)
            )
        }
    }
}

@MultiFormFactorPreviews
@Composable
private fun ToolbarPreview() {
    SoptTheme {
        Box(
            Modifier.fillMaxSize()
        ) {
            Toolbar(
                title = {
                    Text(
                        text = "미션",
                        style = SoptTheme.typography.h2,
                        modifier = Modifier.padding(start = 2.dp),
                        color = SoptTheme.colors.onSurface
                    )
                },
                iconOption = ToolbarIconType.WRITE,
                onBack = {}
            )
        }
    }
}
