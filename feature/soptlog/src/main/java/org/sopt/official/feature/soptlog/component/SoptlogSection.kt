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

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import com.skydoves.balloon.compose.setOverlayColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.feature.soptlog.R
import org.sopt.official.feature.soptlog.SoptLogState
import org.sopt.official.feature.soptlog.model.MySoptLogItemType

@Composable
internal fun SoptlogSection(
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
                MySoptlogRowItem(
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
private fun MySoptlogRowItem(
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mySoptLogItemType.title,
                color = White,
                style = SoptTheme.typography.title14SB
            )
            if (mySoptLogItemType.hasHelpIcon) {
                SoptlogViewCountTooltip()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoptlogViewCountTooltip(
    modifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current
    val xOffset = with(LocalDensity.current) { 48.dp.roundToPx() } // 툴팁 오프셋 설정 (툴팁을 오른쪽으로 이동)
    val spacing = with(LocalDensity.current) { 8.dp.roundToPx() } // 앵커(툴팁 물음표)와 화살표 사이 간격
    val arrowOffsetX = with(density) { (-xOffset).toDp() } // 화살표 offset은 툴팁 offset의 반대 방향

    TooltipBox(
        modifier = modifier,
        positionProvider = remember(xOffset, spacing) {
            CustomTooltipPositionProvider(
                direction = TooltipDirection.Top,
                xOffsetPx = xOffset,
                spacingPx = spacing
            )
        },
        tooltip = {
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoptTheme.colors.onSurface600)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_helper),
                                    contentDescription = null,
                                    tint = SoptTheme.colors.primary,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "조회수",
                                    style = SoptTheme.typography.title14SB,
                                    color = SoptTheme.colors.primary
                                )
                            }
                            IconButton(
                                onClick = { coroutineScope.launch { tooltipState.dismiss() } },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_18),
                                    contentDescription = "닫기",
                                    tint = Color.Unspecified
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

                Canvas(
                    modifier = Modifier
                        .offset(x = arrowOffsetX)
                        .size(width = 12.dp, height = 12.dp)
                ) {
                    drawTooltipArrow(
                        color = Gray600,
                        direction = TooltipDirection.Top
                    )
                }
            }
        },
        state = tooltipState
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_helper),
            contentDescription = "조회수 설명 툴팁",
            tint = SoptTheme.colors.onSurface100,
            modifier = Modifier
                .padding(2.dp)
                .clickable { coroutineScope.launch { tooltipState.show() } }
        )
    }
}

@Composable
private fun SoptlogBalloon(
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

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_18),
                contentDescription = "조회수 툴팁 닫기",
                tint = Color.Unspecified
            )
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
        setDismissWhenOverlayClicked(false)
        setDismissWhenTouchOutside(false)
        setDismissWhenClicked(true)
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

            SoptlogSection(
                title = "솝탬프 로그",
                items = persistentListOf(
                    MySoptLogItemType.COMPLETED_MISSION,
                    MySoptLogItemType.VIEW_COUNT,
                    MySoptLogItemType.RECEIVED_CLAP,
                    MySoptLogItemType.SENT_CLAP
                ),
                soptLogInfo = dummyStats,
                onItemClick = { }
            )

            Spacer(modifier = Modifier.height(28.dp))

            SoptlogSection(
                title = "콕찌르기 로그",
                items = persistentListOf(
                    MySoptLogItemType.TOTAL_POKE,
                    MySoptLogItemType.CLOSE_FRIEND,
                    MySoptLogItemType.BEST_FRIEND,
                    MySoptLogItemType.SOULMATE
                ),
                soptLogInfo = dummyStats,
                onItemClick = { _ -> }
            )
        }
    }
}

/**
 * 툴팁 방향을 정의하는 enum
 * Top    툴팁이 anchor 위쪽에 표시
 * Bottom 툴팁이 anchor 아래쪽에 표시
 * Left   툴팁이 anchor 왼쪽에 표시
 * Right  툴팁이 anchor 오른쪽에 표시
 */
enum class TooltipDirection {
    Top,
    Bottom,
    Left,
    Right
}

/**
 * Canvas를 사용하여 툴팁 화살표를 그리는 함수
 * @param color 화살표의 색상
 * @param direction 화살표가 가리키는 방향 (툴팁의 위치에 따라 결정)
 */
private fun DrawScope.drawTooltipArrow(
    color: Color,
    direction: TooltipDirection
) {
    val path = Path().apply {
        when (direction) {
            TooltipDirection.Top -> {
                moveTo(0f, 0f)
                lineTo(size.width / 2, size.height)
                lineTo(size.width, 0f)
            }
            TooltipDirection.Bottom -> {
                moveTo(0f, size.height)
                lineTo(size.width / 2, 0f)
                lineTo(size.width, size.height)
            }
            TooltipDirection.Left -> {
                moveTo(size.width, size.height / 2)
                lineTo(0f, 0f)
                lineTo(0f, size.height)
            }
            TooltipDirection.Right -> {
                moveTo(0f, size.height / 2)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
            }
        }
        close()
    }
    drawPath(path = path, color = color)
}


/**
 * 툴팁의 위치를 계산하는 커스텀 PopupPositionProvider
 * @param direction 툴팁이 표시될 방향
 * @param xOffsetPx 툴팁의 x축 오프셋 (픽셀 단위)
 * @param spacingPx anchor와 툴팁 사이의 간격 (픽셀 단위)
 */
private class CustomTooltipPositionProvider(
    private val direction: TooltipDirection,
    private val xOffsetPx: Int = 0,
    private val spacingPx: Int = 0,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x: Int
        val y: Int

        val anchorCenterX = anchorBounds.left + (anchorBounds.width / 2)

        when (direction) {
            TooltipDirection.Top -> {
                x = anchorCenterX - (popupContentSize.width / 2) + xOffsetPx
                y = anchorBounds.top - popupContentSize.height - spacingPx
            }
            TooltipDirection.Bottom -> {
                x = anchorCenterX - (popupContentSize.width / 2) + xOffsetPx
                y = anchorBounds.bottom + spacingPx
            }
            else -> {
                x = 0; y = 0
            }
        }
        return IntOffset(x, y)
    }
}