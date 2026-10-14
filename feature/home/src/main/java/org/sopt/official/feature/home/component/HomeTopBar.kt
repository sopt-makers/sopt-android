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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R.drawable.img_logo
import org.sopt.official.mds.MdsIcons

@Composable
internal fun HomeTopBarForMember(
    hasNotification: Boolean,
    onNotificationClick: () -> Unit,
    onLogoLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTopBar(modifier = modifier, onLogoLongClick = onLogoLongClick) {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (hasNotification) MdsIcons.bellActiveFilled else MdsIcons.bellFilled
            ),
            contentDescription = null,
            tint = Unspecified,
            modifier = Modifier
                .size(32.dp)
                .padding(6.dp)
                .clickable(onClick = onNotificationClick),
        )
    }
}

@Preview
@Composable
private fun HomeTopBarForMemberPreview() {
    SoptTheme {
        HomeTopBarForMember(
            hasNotification = true,
            onNotificationClick = {},
            onLogoLongClick = {}
        )
    }
}

@Composable
internal fun HomeTopBarForVisitor(
    onLogoLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTopBar(modifier = modifier, onLogoLongClick = onLogoLongClick) {}
}

@Preview
@Composable
private fun HomeTopBarForVisitorPreview() {
    SoptTheme {
        HomeTopBarForVisitor(
            onLogoLongClick = {}
        )
    }
}


@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    onLogoLongClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        val currentLogoLongClick by rememberUpdatedState(onLogoLongClick)

        Image(
            painter = painterResource(img_logo),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = 72.dp,
                    height = 40.dp,
                )
                // TODO(poke-v2): 로고 롱클릭은 poke v2를 테스트하기 위한 개발자 전용 임시 진입점이다.
                //  정식 연결점이 정해지면 MainTab/Route 기반 네비게이션으로 교체하고 이 진입점은 제거한다.
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { currentLogoLongClick() },
                    )
                }
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
        HomeTopBar {}
    }
}
