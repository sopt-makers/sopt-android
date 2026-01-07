package org.sopt.official.feature.appjamtamp.missiondetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.TwoButtonDialog
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.component.AppjamtampButton
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.missiondetail.component.ClapFeedbackHolder
import org.sopt.official.feature.appjamtamp.missiondetail.component.ClapUserBottomDialog
import org.sopt.official.feature.appjamtamp.missiondetail.component.DataPickerBottomSheet
import org.sopt.official.feature.appjamtamp.missiondetail.component.DatePicker
import org.sopt.official.feature.appjamtamp.missiondetail.component.DetailInfo
import org.sopt.official.feature.appjamtamp.missiondetail.component.ImageContent
import org.sopt.official.feature.appjamtamp.missiondetail.component.ImageModal
import org.sopt.official.feature.appjamtamp.missiondetail.component.Memo
import org.sopt.official.feature.appjamtamp.missiondetail.component.MissionHeader
import org.sopt.official.feature.appjamtamp.missiondetail.component.PostSubmissionBadge
import org.sopt.official.feature.appjamtamp.missiondetail.component.ProfileTag
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun MissionDetailRoute(
    navigateUp: () -> Unit,
    viewModel: MissionDetailViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.missionDetailState.collectAsStateWithLifecycle()

    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isClapBottomSheetVisible by remember { mutableStateOf(false) }
    var isImageZoomInDialogVisible by remember { mutableStateOf(false) }
    var selectedZoomInImage by remember { mutableStateOf<String?>(null) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    var showPostSubmissionBadge by remember(uiState.showPostSubmissionBadge) {
        mutableStateOf(
            uiState.showPostSubmissionBadge
        )
    }

    val lottieResId = remember(uiState.mission.level) {
        when (uiState.mission.level.value) {
            1 -> R.raw.pinkstamps
            2 -> R.raw.purplestamp
            3 -> R.raw.greenstamp
            else -> R.raw.orangestamp
        }
    }

    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieResId),
    )
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        isPlaying = showPostSubmissionBadge,
    )

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        viewModel.flushClap()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is MissionDetailSideEffect.NavigateUp -> navigateUp()
                }
            }
    }

    LaunchedEffect(!uiState.isLoading, progress) {
        if (progress >= 0.99f && !uiState.isLoading) {
            delay(500L)
            viewModel.updateShowPostSubmissionBadge()
        }
    }

    if (uiState.viewType == DetailViewType.WRITE) {
        MyEmptyMissionDetailScreen(
            uiState = uiState,
            onBackButtonClick = navigateUp,
            onChangeImage = viewModel::updateImageModel,
            onClickZoomIn = { image ->
                isImageZoomInDialogVisible = true
                selectedZoomInImage = image
            },
            onDatePickerClick = { isDatePickerVisible = true },
            onMemoChange = viewModel::updateContent,
            onCompleteButtonClick = viewModel::handleSubmit
        )
    } else {
        MissionDetailScreen(
            uiState = uiState,
            onBackButtonClick = navigateUp,
            onToolbarIconClick = {
                when (uiState.viewType) {
                    DetailViewType.COMPLETE -> {
                        viewModel.updateViewType(DetailViewType.EDIT)
                    }

                    DetailViewType.EDIT -> {
                        isDeleteDialogVisible = true
                    }

                    else -> {}
                }
            },
            onChangeImage = viewModel::updateImageModel,
            onClickZoomIn = { image ->
                isImageZoomInDialogVisible = true
                selectedZoomInImage = image
            },
            onDatePickerClick = { isDatePickerVisible = true },
            onMemoChange = viewModel::updateContent,
            onActionButtonClick = {
                when (uiState.viewType) {
                    DetailViewType.WRITE -> viewModel.handleSubmit()
                    DetailViewType.READ_ONLY -> viewModel.onClap()
                    DetailViewType.COMPLETE -> {
                        viewModel.getClappersList()
                        isClapBottomSheetVisible = true
                    }

                    DetailViewType.EDIT -> viewModel.handleSubmit()
                }
            }
        )
    }

    if (isDatePickerVisible) {
        DataPickerBottomSheet(
            onSelected = {
                viewModel.updateDate(it)
                isDatePickerVisible = false
            },
            onDismissRequest = { isDatePickerVisible = false }
        )
    }

    if (isClapBottomSheetVisible) {
        ClapUserBottomDialog(
            onDismiss = { isClapBottomSheetVisible = false },
            userList = uiState.clappers,
            onClickUser = { _, _ -> /* Nothing to do */ }
        )
    }

    if (isImageZoomInDialogVisible) {
        ImageModal(
            image = selectedZoomInImage.orEmpty(),
            onDismiss = {
                isImageZoomInDialogVisible = false
                selectedZoomInImage = null
            }
        )
    }

    if (isDeleteDialogVisible) {
        TwoButtonDialog(
            onDismiss = { isDeleteDialogVisible = false },
            positiveButtonText = "삭제",
            negativeButtonText = "취소",
            onPositiveClick = viewModel::deleteMission,
            onNegativeClick = { isDeleteDialogVisible = false }
        ) {
            Text(
                text = "달성한 미션을 삭제하시겠습니까?",
                style = SoptTheme.typography.body16M,
                color = SoptTheme.colors.primary,
            )
        }
    }

    if (showPostSubmissionBadge) {
        PostSubmissionBadge(
            composition = lottieComposition,
            progress = progress
        )
    }
}

@Composable
private fun MyEmptyMissionDetailScreen(
    uiState: MissionDetailState,
    onBackButtonClick: () -> Unit,
    onChangeImage: (ImageModel) -> Unit,
    onClickZoomIn: (String) -> Unit,
    onDatePickerClick: () -> Unit,
    onMemoChange: (String) -> Unit,
    onCompleteButtonClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        BackButtonHeader(
            title = "미션",
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        MissionHeader(
            title = uiState.mission.title,
            stamp = Stamp.findStampByLevel(uiState.mission.level)
        )

        Spacer(modifier = Modifier.height(5.dp))

        ImageContent(
            imageModel = uiState.imageModel,
            onChangeImage = onChangeImage,
            onClickZoomIn = onClickZoomIn,
            isEditable = true
        )

        Spacer(modifier = Modifier.height(15.dp))

        DatePicker(
            value = uiState.date,
            placeHolder = "날짜를 입력해주세요.",
            isEditable = true,
            onClicked = onDatePickerClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Memo(
            value = uiState.content,
            placeHolder = "함께한 사람과 어떤 추억을 남겼는지 작성해 주세요.",
            onValueChange = onMemoChange,
            isEditable = true
        )

        Spacer(modifier = Modifier.weight(1f))

        AppjamtampButton(
            text = "미션 완료",
            onClicked = onCompleteButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun MissionDetailScreen(
    uiState: MissionDetailState,
    onBackButtonClick: () -> Unit,
    onToolbarIconClick: () -> Unit,
    onChangeImage: (ImageModel) -> Unit,
    onClickZoomIn: (String) -> Unit,
    onDatePickerClick: () -> Unit,
    onMemoChange: (String) -> Unit,
    onActionButtonClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var isEditable by remember(uiState.viewType) { mutableStateOf(uiState.viewType == DetailViewType.EDIT) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .align(Alignment.TopCenter)
        ) {
            BackButtonHeader(
                title = if (uiState.viewType == DetailViewType.COMPLETE) "내 미션" else uiState.teamName,
                onBackButtonClick = onBackButtonClick,
                trailingIcon = {
                    uiState.viewType.toolbarIcon?.let {
                        Icon(
                            imageVector = ImageVector.vectorResource(uiState.viewType.toolbarIcon),
                            contentDescription = null,
                            tint = SoptTheme.colors.onSurface10,
                            modifier = Modifier
                                .clickable(onClick = onToolbarIconClick)
                        )
                    }
                },
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            MissionHeader(
                title = uiState.mission.title,
                stamp = Stamp.findStampByLevel(uiState.mission.level)
            )

            Spacer(modifier = Modifier.height(5.dp))

            ImageContent(
                imageModel = uiState.imageModel,
                onChangeImage = onChangeImage,
                onClickZoomIn = onClickZoomIn,
                isEditable = isEditable
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileTag(
                name = uiState.writer.name,
                profileImage = uiState.writer.profileImage
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditable) {
                DatePicker(
                    value = uiState.date,
                    placeHolder = "날짜를 입력해주세요.",
                    isEditable = true,
                    onClicked = onDatePickerClick
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Memo(
                value = uiState.content,
                placeHolder = "함께한 사람과 어떤 추억을 남겼는지 작성해 주세요.",
                onValueChange = onMemoChange,
                isEditable = isEditable
            )
            Spacer(modifier = Modifier.height(8.dp))

            DetailInfo(
                date = uiState.date,
                clapCount = uiState.clapCount,
                viewCount = uiState.viewCount
            )

            Spacer(modifier = Modifier.height(120.dp))
        }

        when (uiState.viewType) {
            DetailViewType.READ_ONLY -> {
                ClapFeedbackHolder(
                    clapCount = uiState.clapCount,
                    myClapCount = uiState.myClapCount,
                    onPressClap = onActionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            DetailViewType.COMPLETE -> {
                AppjamtampButton(
                    text = "누가 박수쳤을까요?",
                    onClicked = onActionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }

            DetailViewType.EDIT, DetailViewType.WRITE -> {
                AppjamtampButton(
                    text = "미션 완료",
                    onClicked = onActionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun MyEmptyMissionDetailScreenPreview() {
    SoptTheme {
        MyEmptyMissionDetailScreen(
            uiState = MissionDetailState(),
            onBackButtonClick = {},
            onChangeImage = {},
            onClickZoomIn = {},
            onDatePickerClick = {},
            onMemoChange = {},
            onCompleteButtonClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun MyMissionDetailScreenPreview() {
    SoptTheme {
        MissionDetailScreen(
            uiState = MissionDetailState(),
            onBackButtonClick = {},
            onChangeImage = {},
            onClickZoomIn = {},
            onDatePickerClick = {},
            onMemoChange = {},
            onActionButtonClick = {},
            onToolbarIconClick = {}
        )
    }
}
