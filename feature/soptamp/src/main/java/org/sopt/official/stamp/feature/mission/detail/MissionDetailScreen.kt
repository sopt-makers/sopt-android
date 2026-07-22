/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.domain.soptamp.fake.FakeImageUploaderRepository
import org.sopt.official.domain.soptamp.fake.FakeStampRepository
import org.sopt.official.domain.soptamp.fake.FakeUserRepository
import org.sopt.official.model.UserStatus
import org.sopt.official.model.toViewType
import org.sopt.official.stamp.R
import org.sopt.official.stamp.SoptampAnalyticsEvent
import org.sopt.official.stamp.designsystem.component.button.SoptampButton
import org.sopt.official.stamp.designsystem.component.dialog.DoubleOptionDialog
import org.sopt.official.stamp.designsystem.component.toolbar.Toolbar
import org.sopt.official.stamp.feature.mission.detail.component.ClapFeedbackHolder
import org.sopt.official.stamp.feature.mission.detail.component.ClapUserBottomDialog
import org.sopt.official.stamp.feature.mission.detail.component.DataPickerBottomSheet
import org.sopt.official.stamp.feature.mission.detail.component.DatePicker
import org.sopt.official.stamp.feature.mission.detail.component.DetailInfo
import org.sopt.official.stamp.feature.mission.detail.component.DetailSnackBar
import org.sopt.official.stamp.feature.mission.detail.component.Header
import org.sopt.official.stamp.feature.mission.detail.component.ImageContent
import org.sopt.official.stamp.feature.mission.detail.component.ImageModal
import org.sopt.official.stamp.feature.mission.detail.component.Memo
import org.sopt.official.stamp.feature.mission.detail.component.PostSubmissionBadge
import org.sopt.official.stamp.feature.mission.detail.type.MissionDetailModeType
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.navigation.navigateToUserMissionList
import org.sopt.official.stamp.feature.navigation.setMissionDetailResult
import org.sopt.official.stamp.util.DefaultPreview

@Composable
internal fun MissionDetailScreen(
    args: MissionNavArgs,
    navController: NavController,
    userStatus: UserStatus,
    modifier: Modifier = Modifier,
    viewModel: MissionDetailViewModel = hiltViewModel(),
) {
    val (id, title, level, isCompleted, isMe, nickname) = args
    val uiState by viewModel.missionDetailUiState.collectAsStateWithLifecycle()
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateWithLifecycle(false)
    val isEditable = uiState.mode == MissionDetailModeType.WRITE || uiState.mode == MissionDetailModeType.EDIT
    val lottieResId =
        remember(level) {
            when (level.value) {
                1 -> R.raw.pinkstamps
                2 -> R.raw.purplestamp
                3 -> R.raw.greenstamp
                else -> R.raw.orangestamp
            }
        }
    val appliedCount by viewModel.appliedCount.collectAsStateWithLifecycle(0)
    val totalClapCount by viewModel.totalClapCount.collectAsStateWithLifecycle(0)
    val myClapCount by viewModel.myClapCount.collectAsStateWithLifecycle(initialValue = 0)
    val clappers by viewModel.clappers.collectAsStateWithLifecycle(initialValue = persistentListOf())

    val myNickname by viewModel.myNickname.collectAsStateWithLifecycle()

    var isClapUserListOpen by remember { mutableStateOf(false) }

    val viewCount by viewModel.viewCount.collectAsStateWithLifecycle(initialValue = 0)
    val isBadgeVisible by viewModel.isBadgeVisible.collectAsStateWithLifecycle(initialValue = false)

    var isZoomInDialogOpen by remember { mutableStateOf(false) }
    var selectedZoomInImage by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieResId),
    )
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        isPlaying = uiState.isSuccess,
    )
    val scrollState = rememberScrollState()

    val tracker = LocalTracker.current
    val viewType = userStatus.toViewType()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getMyName()
    }

    LaunchedEffect(id, isCompleted, isMe, nickname) {
        viewModel.initMissionState(id, isCompleted, isMe, nickname)
    }
    LaunchedEffect(uiState.isSuccess, progress) {
        if (progress >= 0.99f && uiState.isSuccess) {
            delay(500L)
            navController.setMissionDetailResult(true)
            navController.popBackStack()
        }
    }
    LaunchedEffect(uiState.isDeleteSuccess) {
        if (uiState.isDeleteSuccess) {
            navController.setMissionDetailResult(true)
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.isShowEditSnackBar) {
        if (uiState.isShowEditSnackBar) {
            snackBarHostState.showSnackbar(
                message = "수정 완료되었습니다.",
                duration = SnackbarDuration.Short,
            )
            viewModel.onHideEditSnackBar()
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.flushClapDataOnExit()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(alignment = TopCenter),
                    snackbar = { data ->
                        DetailSnackBar(
                            icon = R.drawable.ic_check,
                            title = data.visuals.message,
                        )
                    },
                )
            }
        },
        containerColor = SoptTheme.colors.onSurface950,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
            ) {
                Toolbar(
                    modifier = Modifier.padding(bottom = 10.dp),
                    title = {
                        Text(
                            text = if (isMe) "미션" else nickname,
                            style = SoptTheme.typography.heading18B,
                            modifier = Modifier.padding(start = 2.dp),
                            color = SoptTheme.colors.onSurface10,
                        )
                    },
                    iconOption = uiState.toolbarIconType,
                    onBack = { navController.popBackStack() },
                    onPressIcon = viewModel::onPressToolbarIcon,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                ) {
                    Header(
                        title = title,
                        stars = level.value,
                        toolbarIconType = uiState.toolbarIconType,
                        isMe = isMe,
                        isCompleted = isCompleted,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ImageContent(
                        imageModel = uiState.imageUri,
                        onChangeImage = viewModel::onChangeImage,
                        isEditable = isEditable && isMe,
                        onClickZoomIn = { url ->
                            isZoomInDialogOpen = true
                            selectedZoomInImage = url
                            tracker.trackViewType(SoptampAnalyticsEvent.CLICK_GET_IMAGE_ZOOM, viewType)
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isEditable && isMe) {
                        DatePicker(
                            value = uiState.date,
                            placeHolder = "날짜를 입력해 주세요.",
                            onClicked = {
                                viewModel.onChangeDatePickerBottomSheetOpened(true)
                            },
                            isEditable = isEditable && isMe && !uiState.isSuccess,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Memo(
                        value = uiState.content,
                        placeHolder = "함께한 사람과 어떤 추억을 남겼는지 작성해 주세요.",
                        onValueChange = viewModel::onChangeContent,
                        borderColor = SoptTheme.colors.onSurface600,
                        isEditable = isEditable && isMe && !uiState.isSuccess,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isCompleted) {
                        DetailInfo(
                            date = uiState.date,
                            clapCount = totalClapCount,
                            viewCount = viewCount,
                        )
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }

            if (isEditable && isMe) {
                SoptampButton(
                    text = if (uiState.mode == MissionDetailModeType.EDIT) "수정 완료" else "미션 완료",
                    onClicked = viewModel::onSubmit,
                    isEnabled = isSubmitEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                )
            }

            if (!isEditable && isMe) {
                SoptampButton(
                    text = "누가 박수쳤을까요?",
                    onClicked = {
                        tracker.trackViewType(SoptampAnalyticsEvent.CLICK_CLAPPERLIST, viewType)
                        isClapUserListOpen = true
                        viewModel.getStampClappers(stampId = uiState.stampId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                )
            }

            if (!isMe) {
                ClapFeedbackHolder(
                    clapCount = totalClapCount,
                    myClapCount = myClapCount,
                    isBadgeVisible = isBadgeVisible,
                    onPressClap = {
                        tracker.trackViewType(SoptampAnalyticsEvent.CLICK_UPDATE_CLAP, viewType)
                        viewModel.onPressClap()
                    },
                )
            }
        }
    }

    if (uiState.isSuccess) {
        PostSubmissionBadge(
            composition = lottieComposition,
            progress = progress,
        )
    }
    if (uiState.isDeleteDialogVisible) {
        DoubleOptionDialog(
            title = "달성한 미션을 삭제하시겠습니까?",
            onCancel = {
                viewModel.onChangeDeleteDialogVisibility(false)
            },
            onConfirm = {
                viewModel.onDelete()
            },
        )
    }
    if (uiState.isError) {
        NetworkErrorDialog(
            onConfirm = viewModel::onPressNetworkErrorDialog,
        )
    }
    if (uiState.isBottomSheetOpened) {
        DataPickerBottomSheet(
            onSelected = { date ->
                viewModel.onChangeDate(date)
                viewModel.onChangeDatePickerBottomSheetOpened(false)
            },
            onDismissRequest = { viewModel.onChangeDatePickerBottomSheetOpened(false) },
        )
    }
    if (isZoomInDialogOpen) {
        ImageModal(
            image = selectedZoomInImage ?: "",
            onDismiss = {
                isZoomInDialogOpen = false
                selectedZoomInImage = null
            },
        )
    }
    if (isClapUserListOpen) {
        ClapUserBottomDialog(
            onDismiss = { isClapUserListOpen = false },
            userList = clappers,
            onClickUser = { nickname, description ->
                navController.navigateToUserMissionList(
                    nickname = nickname.toString(),
                    description = description ?: "설정된 한 마디가 없습니다.",
                    entrySource = "clappersList"
                )
            }
        )
    }
}

// Simple no-op NavController for previews
private class PreviewNavController(context: Context) : NavController(context) {
    override fun popBackStack(): Boolean = true
}


@DefaultPreview
@Composable
fun MissionDetailPreview() {
    val context = LocalContext.current
    val args =
        MissionNavArgs(
            id = 1,
            title = "앱잼 팀원 다 함께 바다 보고 오기",
            level = MissionLevel.of(2),
            isCompleted = false,
            isMe = false,
            nickname = "Nunu",
        )

    @SuppressLint("ViewModelConstructorInComposable")
    val fakeViewModel = MissionDetailViewModel(
        stampRepository = FakeStampRepository,
        imageUploaderRepository = FakeImageUploaderRepository,
        userRepository = FakeUserRepository
    )

    SoptTheme {
        MissionDetailScreen(
            args = args,
            navController = PreviewNavController(context),
            viewModel = fakeViewModel,
            userStatus = UserStatus.ACTIVE
        )
    }
}
