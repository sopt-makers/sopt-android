/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterMemoDetailDialog(
    state: SopletterMemoDetailDialogContract.State,
    actions: SopletterMemoDetailDialogContract.Actions,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = actions::onDismissClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BoxWithConstraints(
            modifier = Modifier.padding(20.dp),
        ) {
            val scrollState = rememberScrollState()
            val dialogMaxHeight = maxHeight * 0.7f
            val dialogMinHeight = minOf(338.dp, dialogMaxHeight)

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = dialogMinHeight,
                        max = dialogMaxHeight,
                    )
                    .background(
                        color = state.memoColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = state.writerName,
                        style = SoptTheme.typography.title16SB,
                        color = SoptTheme.colors.onSurface600,
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 4.dp,
                            ),
                    )

                    when (state.isMine) {
                        true -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_edit_32),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .noRippleClickable(actions::onEditClick),
                                )

                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_trash_32),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .noRippleClickable(actions::onDeleteClick),
                                )
                            }
                        }

                        false -> Unit
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = state.content,
                            style = SoptTheme.typography.body16R,
                            color = SoptTheme.colors.onSurface600,
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(scrollState),
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(12.dp)
                                .padding(4.dp)
                                .verticalScrollbar(
                                    scrollState = scrollState,
                                    thumbWidth = 4.dp,
                                    thumbHeight = 220.dp,
                                    thumbColor = SoptTheme.colors.onSurface500,
                                ),
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = state.date,
                        style = SoptTheme.typography.body16R,
                        color = SoptTheme.colors.onSurface300,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.noRippleClickable(actions::onLikeClick),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                if (state.isLiked) R.drawable.ic_active_heart_24 else R.drawable.ic_inactive_heart_24
                            ),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = "${state.likeCount}",
                            style = SoptTheme.typography.body16R,
                            color = SoptTheme.colors.onSurface600,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SoptTheme.colors.onSurface800,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .noRippleClickable(actions::onDismissClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "확인",
                        style = SoptTheme.typography.title16SB,
                        color = SoptTheme.colors.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(14.dp),
                    )
                }
            }
        }
    }
}
