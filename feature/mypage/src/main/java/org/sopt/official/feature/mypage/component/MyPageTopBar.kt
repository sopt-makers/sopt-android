/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.theme.SoptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .padding(horizontal = 20.dp),
        title = {
            Text(
                text = title,
                style = SoptTheme.typography.heading4
            )
        },
        navigationIcon = {
            if (onNavigationIconClick != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(onClick = onNavigationIconClick),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SoptTheme.colors.bg.layer.basement,
            titleContentColor = SoptTheme.colors.fg.neutral.bold,
            navigationIconContentColor = SoptTheme.colors.fg.neutral.bold
        )
    )
}
