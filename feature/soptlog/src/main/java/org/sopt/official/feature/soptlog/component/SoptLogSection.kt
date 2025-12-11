/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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

package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.skydoves.balloon.compose.setOverlayColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.feature.soptlog.R
import org.sopt.official.feature.soptlog.model.MySoptLogItemType
import org.sopt.official.feature.soptlog.model.SoptLogCategory
import org.sopt.official.feature.soptlog.state.SoptLogState

@Composable
internal fun SoptLogEmptySection(
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = SoptTheme.colors.onSurface900)
            .padding(horizontal = 18.dp)
            .padding(top = 48.dp, bottom = 54.dp),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_soptlog_empty_view_eyes),
            contentDescription = null,
            tint = SoptTheme.colors.onSurface700
        )

        Text(
            text = content,
            color = SoptTheme.colors.onSurface500,
            style = SoptTheme.typography.body14M,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun SoptLogSection(
    title: String,
    items: ImmutableList<MySoptLogItemType>,
    soptLogInfo: SoptLogInfo,
    onItemClick: (MySoptLogItemType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = White,
            style = SoptTheme.typography.title16SB,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = SoptTheme.colors.onSurface900)
                .padding(vertical = 6.dp)
        ) {
            items.forEachIndexed { index, type ->
                MySoptLogRowItem(
                    onClick = { onItemClick(type) },
                    soptLogInfo = soptLogInfo,
                    mySoptLogItemType = type
                )

                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal =16.dp),
                        thickness = 1.dp,
                        color = SoptTheme.colors.onSurface800
                    )
                }
            }
        }
    }
}

@Composable
private fun MySoptLogRowItem(
    onClick: () -> Unit,
    soptLogInfo: SoptLogInfo,
    mySoptLogItemType: MySoptLogItemType
) {
    val count = mySoptLogItemType.count(soptLogInfo)
    val balloonBuilder = rememberCustomBalloonBuilder()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, bottom = 13.dp, start = 20.dp, end = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mySoptLogItemType.title,
                color = White,
                style = SoptTheme.typography.title14SB
            )
            if (mySoptLogItemType.hasHelpIcon) {
                Balloon(
                    builder = balloonBuilder,
                    balloonContent = {
                        SoptLogBalloon(modifier = Modifier.width(IntrinsicSize.Max))
                    }
                ) { balloon ->
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_helper),
                        contentDescription = "조회수 설명 툴팁",
                        tint = SoptTheme.colors.onSurface100,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 2.dp, start = 2.dp)
                            .clickable { balloon.showAlignTop() }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.clickable(
                enabled = mySoptLogItemType.hasArrow,
                onClick = onClick
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${count}회",
                color = White,
                style = SoptTheme.typography.title14SB
            )
            if (mySoptLogItemType.hasArrow) {
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_soptlog_arrow_right),
                    contentDescription = "${mySoptLogItemType.title} 이동",
                    tint = SoptTheme.colors.onSurface200,
                    modifier = Modifier.size(size = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun SoptLogBalloon(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 18.dp, vertical = 16.dp)
            .background(color = SoptTheme.colors.onSurface600)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_helper),
                    contentDescription = null,
                    tint = SoptTheme.colors.primary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "조회수",
                    style = SoptTheme.typography.title14SB,
                    color = SoptTheme.colors.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "솝트 전체 회원들이 내 솝탬프 미션을\n조회한 횟수를 의미해요.",
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface50,
            modifier = Modifier.padding(end = 34.dp)
        )
    }
}

@Composable
private fun rememberCustomBalloonBuilder(): Balloon.Builder {
    val color = SoptTheme.colors
    return rememberBalloonBuilder {
        setArrowSize(12)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowOrientation(ArrowOrientation.TOP)
        setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
        setBackgroundColor(color.onSurface600)
        setCornerRadius(12f)
        setMarginLeft(60)
        setMarginRight(40)
        setHeight(BalloonSizeSpec.WRAP)
        setWidth(BalloonSizeSpec.WRAP)
        setIsVisibleOverlay(true)
        setOverlayColor(color.onSurface.copy(alpha = 0.5f))
        setDismissWhenOverlayClicked(true)
        setDismissWhenTouchOutside(true)
        setDismissWhenClicked(false)
        setElevation(4)
    }
}

@Preview(showBackground = true)
@Composable
private fun SoptLogSectionPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray950)
            .padding(all = 16.dp)
    ) {
        Column {
            val dummyStats = SoptLogState().soptLogInfo

            SoptLogSection(
                title = "솝탬프 로그",
                items = MySoptLogItemType.entries.filter { it.category == SoptLogCategory.SOPTAMP }.toImmutableList(),
                soptLogInfo = dummyStats,
                onItemClick = { }
            )

            Spacer(modifier = Modifier.height(28.dp))

            SoptLogSection(
                title = "콕찌르기 로그",
                items = MySoptLogItemType.entries.filter { it.category == SoptLogCategory.POKE }.toImmutableList(),
                soptLogInfo = dummyStats,
                onItemClick = { _ -> }
            )
        }
    }
}
