/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.util.MultiFormFactorPreviews
import org.sopt.official.feature.appjamtamp.util.noRippleClickable

enum class ToolbarIconType(
    @field:DrawableRes private val resId: Int = -1,
) {
    NONE,
    WRITE(R.drawable.ic_write),
    DELETE(R.drawable.ic_delete),
    ;

    companion object {
        @Composable
        fun getResourceFrom(type: ToolbarIconType) =
            painterResource(
                entries.find { it.name == type.name }?.resId
                    ?: throw IllegalArgumentException("There's no icon in this class. Icon: ${type.name}"),
            )
    }
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    iconOption: ToolbarIconType = ToolbarIconType.NONE,
    onBack: (() -> Unit)? = null,
    onPressIcon: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = "Back Button",
                    tint = SoptTheme.colors.onSurface10,
                    modifier =
                        Modifier.Companion
                            .noRippleClickable(onClick = onBack)
                            .align(Alignment.CenterVertically)
                            .padding(8.dp),
                )
            }
            title?.invoke()
        }

        if (iconOption != ToolbarIconType.NONE) {
            Icon(
                painter = ToolbarIconType.getResourceFrom(iconOption),
                contentDescription = "Option Menu Icon",
                tint = SoptTheme.colors.onSurface10,
                modifier = Modifier.noRippleClickable(onClick = onPressIcon),
            )
        }
    }
}

@MultiFormFactorPreviews
@Composable
private fun ToolbarPreview() {
    SoptTheme {
        Box(
            Modifier.fillMaxSize(),
        ) {
            Toolbar(
                title = {
                    Text(
                        text = "미션",
                        style = SoptTheme.typography.heading18B,
                        modifier = Modifier.padding(start = 2.dp),
                        color = SoptTheme.colors.onSurface,
                    )
                },
                iconOption = ToolbarIconType.WRITE,
                onBack = {},
            )
        }
    }
}
