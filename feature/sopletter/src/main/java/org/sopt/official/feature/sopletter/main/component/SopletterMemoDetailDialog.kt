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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterSnackbarHost
import org.sopt.official.feature.sopletter.common.util.consumeClicks
import org.sopt.official.feature.sopletter.common.util.characterCount
import org.sopt.official.feature.sopletter.component.verticalScrollbar
import org.sopt.official.feature.sopletter.main.contract.SOPLETTER_MEMO_MAX_LENGTH
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.sopletter.R

private const val MEMO_DIALOG_OVERLAY_ALPHA = 0.7f

@Composable
internal fun SopletterMemoDetailDialog(
    state: SopletterMemoDetailDialogContract.State,
    actions: SopletterMemoDetailDialogContract.Actions,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val editTextState = rememberTextFieldState(initialText = state.content)
    val editCharacterCount = editTextState.text.characterCount()
    val isOverLengthLimit = editCharacterCount > SOPLETTER_MEMO_MAX_LENGTH

    LaunchedEffect(state.isEditing, isOverLengthLimit) {
        if (state.isEditing && isOverLengthLimit) {
            actions.showMemoLengthWarning()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.onSurface950.copy(alpha = MEMO_DIALOG_OVERLAY_ALPHA))
            .noRippleClickable(actions::onDismissClick),
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            val dialogMaxHeight = maxHeight * 0.7f
            val dialogMinHeight = if (state.isEditing) minOf(338.dp, dialogMaxHeight) else 0.dp

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(20.dp)
                    .then(if (state.isEditing) Modifier.imePadding() else Modifier)
                    .heightIn(
                        min = dialogMinHeight,
                        max = dialogMaxHeight,
                    )
                    .background(
                        color = state.memoColor,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .consumeClicks()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MemoDialogHeader(
                    writerName = state.writerName,
                    isEditing = state.isEditing,
                    isMine = state.isMine,
                    onEditClick = actions::onEditClick,
                    onDeleteClick = actions::onDeleteClick,
                    onEditCancelClick = actions::onEditCancelClick,
                )

                MemoDialogContent(
                    content = state.content,
                    isEditing = state.isEditing,
                    editTextState = editTextState,
                    scrollState = scrollState,
                )

                if (state.isEditing) {
                    Text(
                        text = "$editCharacterCount/$SOPLETTER_MEMO_MAX_LENGTH",
                        style = SoptTheme.typography.body14M,
                        color = if (isOverLengthLimit) {
                            SoptTheme.colors.error
                        } else {
                            SoptTheme.colors.onSurface300
                        },
                        modifier = Modifier.align(Alignment.End),
                    )
                } else {
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
                }

                MemoDialogActionButton(
                    isEditing = state.isEditing,
                    isOverLengthLimit = isOverLengthLimit,
                    onClick = {
                        if (state.isEditing) {
                            actions.onEditCompleteClick(editTextState.text.toString())
                        } else {
                            actions.onDismissClick()
                        }
                    },
                )
            }

        }

        SopletterSnackbarHost(
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )
    }
}

@Composable
private fun MemoDialogActionButton(
    isEditing: Boolean,
    isOverLengthLimit: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDisabled = isEditing && isOverLengthLimit
    val backgroundColor =
        if (isDisabled) SoptTheme.colors.onSurface100 else SoptTheme.colors.onSurface800
    val contentColor =
        if (isDisabled) SoptTheme.colors.onSurface300 else SoptTheme.colors.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp),
            )
            .then(
                if (!isDisabled) {
                    Modifier.noRippleClickable(onClick)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isEditing) "수정 완료" else "확인",
            style = SoptTheme.typography.title16SB,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(14.dp),
        )
    }
}

@Composable
private fun MemoDialogHeader(
    writerName: String,
    isEditing: Boolean,
    isMine: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = writerName,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface600,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        )

        when {
            isEditing -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_32),
                    contentDescription = null,
                    tint = SoptTheme.colors.onSurface500,
                    modifier = Modifier.noRippleClickable(onEditCancelClick),
                )
            }

            isMine -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_edit_32),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.noRippleClickable(onEditClick),
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_trash_32),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.noRippleClickable(onDeleteClick),
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ColumnScope.MemoDialogContent(
    content: String,
    isEditing: Boolean,
    editTextState: TextFieldState,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (isEditing) {
                BasicTextField(
                    state = editTextState,
                    textStyle = SoptTheme.typography.body16R.copy(
                        color = SoptTheme.colors.onSurface600,
                    ),
                    cursorBrush = SolidColor(SoptTheme.colors.onSurface600),
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                )
            } else {
                Text(
                    text = content,
                    style = SoptTheme.typography.body16R,
                    color = SoptTheme.colors.onSurface600,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                )
            }

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
}
