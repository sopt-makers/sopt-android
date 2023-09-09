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
package org.sopt.official.stamp.feature.mission.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.dialog.DoubleOptionDialog
import org.sopt.official.stamp.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.MissionLevel
import org.sopt.official.stamp.domain.fake.FakeStampRepository
import org.sopt.official.stamp.feature.mission.detail.component.Header
import org.sopt.official.stamp.feature.mission.detail.component.ImageContent
import org.sopt.official.stamp.feature.mission.detail.component.Memo
import org.sopt.official.stamp.feature.mission.detail.component.PostSubmissionBadge
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.ranking.getLevelBackgroundColor
import org.sopt.official.stamp.feature.ranking.getLevelTextColor
import org.sopt.official.stamp.feature.ranking.getRankTextColor
import org.sopt.official.stamp.util.DefaultPreview
import org.sopt.official.stamp.feature.mission.model.ImageModel

@MissionNavGraph
@Destination("detail")
@Composable
fun MissionDetailScreen(
    args: MissionNavArgs,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: MissionDetailViewModel = hiltViewModel()
) {
    val (id, title, level, isCompleted, isMe, nickname) = args
    val content by viewModel.content.collectAsState("")
    val imageModel by viewModel.imageModel.collectAsState(ImageModel.Empty)
    val isSuccess by viewModel.isSuccess.collectAsState(false)
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsState(false)
    val toolbarIconType by viewModel.toolbarIconType.collectAsState(ToolbarIconType.NONE)
    val isEditable by viewModel.isEditable.collectAsState(true)
    val createdAt by viewModel.createdAt.collectAsState("")
    val isDeleteSuccess by viewModel.isDeleteSuccess.collectAsState(false)
    val isDeleteDialogVisible by viewModel.isDeleteDialogVisible.collectAsState(false)
    val isError by viewModel.isError.collectAsState(false)
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
        resultNavigator.navigateBack(true)
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
                Spacer(modifier = Modifier.height(12.dp))
                Memo(
                    value = content,
                    placeHolder = "메모를 작성해 주세요.",
                    onValueChange = viewModel::onChangeContent,
                    borderColor = getRankTextColor(level.value),
                    isEditable = isEditable && isMe && !isSuccess
                )
                if (!isEditable || !isMe) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = createdAt,
                            style = SoptTheme.typography.caption4.copy(fontSize = 10.sp),
                            color = SoptTheme.colors.onSurface60
                        )
                    }
                }
            }

            if (isEditable && isMe) {
                Button(
                    onClick = { viewModel.onSubmit() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    enabled = isSubmitEnabled,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = getLevelTextColor(level.value),
                        disabledBackgroundColor = getLevelBackgroundColor(level.value)
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
    }
}

@DefaultPreview
@Composable
fun MissionDetailPreview() {
    val args = MissionNavArgs(
        id = 1,
        title = "앱잼 팀원 다 함께 바다 보고 오기",
        level = MissionLevel.of(2),
        isCompleted = false,
        isMe = true,
        nickname = "Nunu",
    )
    SoptTheme {
        MissionDetailScreen(
            args,
            EmptyResultBackNavigator(),
            MissionDetailViewModel(FakeStampRepository)
        )
    }
}
