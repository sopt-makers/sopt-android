package org.sopt.official.feature.appjamtamp.missiondetail

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
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
import org.sopt.official.feature.appjamtamp.missiondetail.component.ProfileTag
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun MissionDetailRoute(
    navigateUp: () -> Unit,
    viewModel: MissionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.missionDetailState.collectAsStateWithLifecycle()

    var isClapBottomSheetVisible by remember { mutableStateOf(false) }
    var isImageZoomInDialogVisible by remember { mutableStateOf(false) }
    var selectedZoomInImage by remember { mutableStateOf<String?>(null) }

    if (uiState.viewType == DetailViewType.WRITE) {
        MyEmptyMissionDetailScreen(
            uiState = uiState,
            onBackButtonClick = navigateUp,
            onChangeImage = viewModel::updateImageModel,
            onClickZoomIn = { image ->
                isImageZoomInDialogVisible = true
                selectedZoomInImage = image
            },
            onDateSelected = viewModel::updateDate,
            onMemoChange = viewModel::updateContent,
            onCompleteButtonClick = viewModel::onSubmit
        )
    } else {
        MissionDetailScreen(
            uiState = uiState,
            onBackButtonClick = navigateUp,
            onToolbarIconClick = viewModel::updateViewType,
            onChangeImage = viewModel::updateImageModel,
            onClickZoomIn = { image ->
                isImageZoomInDialogVisible = true
                selectedZoomInImage = image
            },
            onDateSelected = viewModel::updateDate,
            onMemoChange = viewModel::updateContent,
            onActionButtonClick = {
                when (uiState.viewType) {
                    DetailViewType.WRITE -> viewModel.onSubmit()
                    DetailViewType.READ_ONLY -> viewModel.onClap()
                    DetailViewType.COMPLETE -> {
                        isClapBottomSheetVisible = true
                    }

                    DetailViewType.EDIT -> viewModel.onSubmit()
                }
            }
        )
    }

    if (isClapBottomSheetVisible) {
        ClapUserBottomDialog(
            onDismiss = { isClapBottomSheetVisible = false },
            userList = persistentListOf(),
            onClickUser = { _, _ -> }
        )
    }

    if (isImageZoomInDialogVisible) {
        ImageModal(
            image = selectedZoomInImage.orEmpty(),
            onDismiss = {
                isImageZoomInDialogVisible = false
                selectedZoomInImage = null
            },
        )
    }
}

@Composable
private fun MyEmptyMissionDetailScreen(
    uiState: MissionDetailState,
    onBackButtonClick: () -> Unit,
    onChangeImage: (ImageModel) -> Unit,
    onClickZoomIn: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onCompleteButtonClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    var isDatePickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        BackButtonHeader(
            title = uiState.header,
            onBackButtonClick = onBackButtonClick
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
            onClicked = { isDatePickerVisible = true }
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

    if (isDatePickerVisible) {
        DataPickerBottomSheet(
            onSelected = onDateSelected,
            onDismissRequest = { isDatePickerVisible = false }
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
    onMemoChange: (String) -> Unit,
    onActionButtonClick: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isEditable by remember(uiState.viewType) { mutableStateOf(uiState.viewType == DetailViewType.EDIT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        BackButtonHeader(
            title = uiState.header,
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
            }
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
                onClicked = { isDatePickerVisible = true }
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

        Spacer(modifier = Modifier.weight(1f))

        when (uiState.viewType) {
            DetailViewType.READ_ONLY -> {
                ClapFeedbackHolder(
                    clapCount = uiState.clapCount,
                    myClapCount = uiState.myClapCount,
                    onPressClap = onActionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
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

    if (isDatePickerVisible) {
        DataPickerBottomSheet(
            onSelected = onDateSelected,
            onDismissRequest = { isDatePickerVisible = false }
        )
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
            onMemoChange = {},
            onCompleteButtonClick = {},
            onDateSelected = {}
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
            onMemoChange = {},
            onDateSelected = {},
            onActionButtonClick = {},
            onToolbarIconClick = {}
        )
    }
}
