/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.mission.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.domain.soptamp.fake.FakeImageUploaderRepository
import org.sopt.official.domain.soptamp.fake.FakeStampRepository
import org.sopt.official.domain.soptamp.model.ImageModel
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.dialog.DoubleOptionDialog
import org.sopt.official.stamp.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.mission.detail.component.DataPickerBottomSheet
import org.sopt.official.stamp.feature.mission.detail.component.DatePicker
import org.sopt.official.stamp.feature.mission.detail.component.Header
import org.sopt.official.stamp.feature.mission.detail.component.ImageContent
import org.sopt.official.stamp.feature.mission.detail.component.Memo
import org.sopt.official.stamp.feature.mission.detail.component.PostSubmissionBadge
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.ranking.getLevelBackgroundColor
import org.sopt.official.stamp.feature.ranking.getLevelTextColor
import org.sopt.official.stamp.util.DefaultPreview

@MissionNavGraph
@Destination("detail")
@Composable
fun MissionDetailScreen(
    args: MissionNavArgs,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: MissionDetailViewModel = hiltViewModel(),
) {
    val (id, title, level, isCompleted, isMe, nickname) = args
    val content by viewModel.content.collectAsStateWithLifecycle("")
    val date by viewModel.date.collectAsStateWithLifecycle("")
    val imageModel by viewModel.imageModel.collectAsStateWithLifecycle(ImageModel.Empty)
    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle(false)
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateWithLifecycle(false)
    val toolbarIconType by viewModel.toolbarIconType.collectAsStateWithLifecycle(ToolbarIconType.NONE)
    val isEditable by viewModel.isEditable.collectAsStateWithLifecycle(true)
    val isDeleteSuccess by viewModel.isDeleteSuccess.collectAsStateWithLifecycle(false)
    val isDeleteDialogVisible by viewModel.isDeleteDialogVisible.collectAsStateWithLifecycle(false)
    val isError by viewModel.isError.collectAsStateWithLifecycle(false)
    val isBottomSheetOpened by viewModel.isBottomSheetOpened.collectAsStateWithLifecycle(false)
    val lottieResId = remember(level) {
        when (level.value) {
            1 -> R.raw.pinkstamps
            2 -> R.raw.purplestamp
            else -> R.raw.greenstamp
        }
    }
    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieResId)
    )
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        isPlaying = isSuccess
    )

    LaunchedEffect(id, isCompleted, isMe, nickname) {
        viewModel.initMissionState(id, isCompleted, isMe, nickname)
    }
    LaunchedEffect(isSuccess, progress) {
        if (progress >= 0.99f && isSuccess) {
            delay(500L)
            resultNavigator.navigateBack(true)
        }
    }
    LaunchedEffect(isDeleteSuccess) {
        if (isDeleteSuccess) {
            resultNavigator.navigateBack(true)
        }
    }

    SoptTheme {
        SoptColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.white)
        ) {
            Toolbar(
                modifier = Modifier.padding(bottom = 10.dp),
                title = {
                    Text(
                        text = "미션",
                        style = SoptTheme.typography.h2,
                        modifier = Modifier.padding(start = 4.dp),
                        color = SoptTheme.colors.onSurface
                    )
                },
                iconOption = toolbarIconType,
                onBack = { resultNavigator.navigateBack() },
                onPressIcon = { viewModel.onPressToolbarIcon() }
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Header(
                    title = title,
                    stars = level.value,
                    toolbarIconType = toolbarIconType,
                    isMe = isMe,
                    isCompleted = isCompleted
                )
                Spacer(modifier = Modifier.height(12.dp))
                ImageContent(
                    imageModel = imageModel,
                    onChangeImage = viewModel::onChangeImage,
                    isEditable = isEditable && isMe
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePicker(
                    value = date,
                    placeHolder = "날짜를 입력해 주세요.",
                    onClicked = {
                        viewModel.onChangeDatePickerBottomSheetOpened(true)
                    },
                    borderColor = getLevelTextColor(level.value),
                    isEditable = isEditable && isMe && !isSuccess
                )
                Spacer(modifier = Modifier.height(8.dp))
                Memo(
                    value = content,
                    placeHolder = "함께한 사람과 어떤 추억을 남겼는지 작성해 주세요.",
                    onValueChange = viewModel::onChangeContent,
                    borderColor = getLevelTextColor(level.value),
                    isEditable = isEditable && isMe && !isSuccess
                )
            }

            if (isEditable && isMe) {
                Button(
                    onClick = {
                        viewModel.onSubmit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    enabled = isSubmitEnabled,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getLevelTextColor(level.value),
                        disabledContainerColor = getLevelBackgroundColor(level.value)
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = "미션 완료",
                        style = SoptTheme.typography.h2,
                        color = if (level.value == 3) SoptTheme.colors.onSurface70 else SoptTheme.colors.white
                    )
                }
            }
        }
        if (isSuccess) {
            PostSubmissionBadge(
                composition = lottieComposition,
                progress = progress
            )
        }
        if (isDeleteDialogVisible) {
            DoubleOptionDialog(
                title = "달성한 미션을 삭제하시겠습니까?",
                onCancel = {
                    viewModel.onChangeDeleteDialogVisibility(false)
                },
                onConfirm = {
                    viewModel.onDelete()
                }
            )
        }
        if (isError) {
            NetworkErrorDialog {
                viewModel.onPressNetworkErrorDialog()
            }
        }
        if (isBottomSheetOpened) {
            DataPickerBottomSheet(
                onSelected = { date ->
                    viewModel.onChangeDate(date)
                    viewModel.onChangeDatePickerBottomSheetOpened(false)
                },
                onDismissRequest = { viewModel.onChangeDatePickerBottomSheetOpened(false) }
            )
        }
    }
}

@DefaultPreview
@Composable
fun MissionDetailPreview() {
    val args = MissionNavArgs(
        id = 1,
        title = "앱잼 팀원 다 함께 바다 보고 오기",
        level = MissionLevel.of(2),
        isCompleted = true,
        isMe = true,
        nickname = "Nunu",
    )
    SoptTheme {
        MissionDetailScreen(
            args,
            EmptyResultBackNavigator(),
            MissionDetailViewModel(stampRepository = FakeStampRepository, imageUploaderRepository = FakeImageUploaderRepository)
        )
    }
}
