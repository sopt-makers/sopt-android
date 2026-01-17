/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R.drawable.ic_notification_off
import org.sopt.official.feature.home.R.drawable.ic_notification_on
import org.sopt.official.feature.home.R.drawable.ic_setting
import org.sopt.official.feature.home.R.drawable.img_logo

@Composable
fun HomeTopBarForMember(
    hasNotification: Boolean,
    onNotificationClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTopBar(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (hasNotification) ic_notification_on else ic_notification_off
            ),
            contentDescription = null,
            tint = Unspecified,
            modifier = Modifier.clickable(onClick = onNotificationClick),
        )
        Icon(
            imageVector = ImageVector.vectorResource(ic_setting),
            contentDescription = null,
            tint = Unspecified,
            modifier = Modifier.clickable(onClick = onSettingClick),
        )
    }
}

@Preview
@Composable
private fun HomeTopBarForMemberPreview() {
    SoptTheme {
        HomeTopBarForMember(
            hasNotification = true,
            onSettingClick = {},
            onNotificationClick = {},
        )
    }
}

@Composable
fun HomeTopBarForVisitor(
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTopBar(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_setting),
            contentDescription = null,
            tint = Unspecified,
            modifier = Modifier.clickable { onSettingClick() },
        )
    }
}

@Preview
@Composable
private fun HomeTopBarForVisitorPreview() {
    SoptTheme {
        HomeTopBarForVisitor(
            onSettingClick = {},
        )
    }
}


@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(img_logo),
            contentDescription = null,
            modifier = Modifier.size(
                width = 72.dp,
                height = 40.dp,
            )
        )
        Row(
            horizontalArrangement = spacedBy(space = 10.dp),
            verticalAlignment = CenterVertically,
            content = content,
        )
    }
}

@Preview
@Composable
private fun HomeTopBarPreview() {
    SoptTheme {
        HomeTopBar {
            Icon(
                imageVector = ImageVector.vectorResource(ic_setting),
                contentDescription = null,
                tint = Unspecified,
            )
        }
    }
}
