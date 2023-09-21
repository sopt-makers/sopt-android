/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.component.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun SoptampFloatingButton(
    text: String,
    onClick: () -> Unit = {}
) {
    ExtendedFloatingActionButton(
        text = {
            Text(
                text = text,
                color = Color.White,
                style = SoptTheme.typography.h2
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Extended Floating Action Button Trophy Icon",
                tint = Color.White
            )
        },
        onClick = { onClick() },
        shape = RoundedCornerShape(46.dp),
        backgroundColor = SoptTheme.colors.purple300
    )
}

@DefaultPreview
@Composable
private fun SoptampFloatingButtonPreview() {
    SoptTheme {
        SoptampFloatingButton(text = "랭킹 보기") { }
    }
}
