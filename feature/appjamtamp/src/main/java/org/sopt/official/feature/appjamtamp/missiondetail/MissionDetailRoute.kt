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
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.AppjamtampButton
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.missiondetail.component.ClapFeedbackHolder
import org.sopt.official.feature.appjamtamp.missiondetail.component.ClapUserBottomDialog
import org.sopt.official.feature.appjamtamp.missiondetail.component.DataPickerBottomSheet
import org.sopt.official.feature.appjamtamp.missiondetail.component.DatePicker
import org.sopt.official.feature.appjamtamp.missiondetail.component.DetailInfo
import org.sopt.official.feature.appjamtamp.missiondetail.component.ImageContent
import org.sopt.official.feature.appjamtamp.missiondetail.component.Memo
import org.sopt.official.feature.appjamtamp.missiondetail.component.MissionHeader
import org.sopt.official.feature.appjamtamp.missiondetail.component.ProfileTag
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.Stamp
import org.sopt.official.feature.appjamtamp.model.User

@Composable
internal fun MissionDetailRoute(

) {
    var isClapBottomSheetVisible by remember { mutableStateOf(false) }

    if (isClapBottomSheetVisible) {
        ClapUserBottomDialog(
            onDismiss = { isClapBottomSheetVisible = false },
            userList = persistentListOf(),
            onClickUser = { _, _ -> }
        )
    }
}

@Composable
private fun MyEmptyMissionDetailScreen(
    mission: Mission,
    imageModel: ImageModel,
    date: String,
    content: String,
    onBackButtonClick: () -> Unit,
    onChangeImage: (ImageModel) -> Unit,
    onClickZoomIn: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onCompleteButtonClick: () -> Unit,
    onDateSelected: (String) -> Unit
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
            title = "미션",
            onBackButtonClick = onBackButtonClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        MissionHeader(
            title = mission.title,
            stamp = Stamp.findStampByLevel(mission.level)
        )

        Spacer(modifier = Modifier.height(5.dp))

        ImageContent(
            imageModel = imageModel,
            onChangeImage = onChangeImage,
            onClickZoomIn = onClickZoomIn,
            isEditable = true
        )

        Spacer(modifier = Modifier.height(15.dp))

        DatePicker(
            value = date,
            placeHolder = "날짜를 입력해주세요.",
            isEditable = true,
            onClicked = { isDatePickerVisible = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Memo(
            value = content,
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
    viewType: DetailViewType,
    title: String,
    mission: Mission,
    imageModel: ImageModel,
    date: String,
    content: String,
    writer: User,
    clapCount: Int,
    myClapCount: Int,
    viewCount: Int,
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
    var isEditable by remember(viewType) { mutableStateOf(viewType == DetailViewType.EDIT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        BackButtonHeader(
            title = title,
            onBackButtonClick = onBackButtonClick,
            trailingIcon = {
                viewType.toolbarIcon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(viewType.toolbarIcon),
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
            title = mission.title,
            stamp = Stamp.findStampByLevel(mission.level)
        )

        Spacer(modifier = Modifier.height(5.dp))

        ImageContent(
            imageModel = imageModel,
            onChangeImage = onChangeImage,
            onClickZoomIn = onClickZoomIn,
            isEditable = isEditable
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileTag(
            name = writer.name,
            profileImage = writer.profileImage
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditable) {
            DatePicker(
                value = date,
                placeHolder = "날짜를 입력해주세요.",
                isEditable = true,
                onClicked = { isDatePickerVisible = true }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Memo(
            value = content,
            placeHolder = "함께한 사람과 어떤 추억을 남겼는지 작성해 주세요.",
            onValueChange = onMemoChange,
            isEditable = isEditable
        )
        Spacer(modifier = Modifier.height(8.dp))

        DetailInfo(
            date = date,
            clapCount = clapCount,
            viewCount = viewCount
        )

        Spacer(modifier = Modifier.weight(1f))

        when (viewType) {
            DetailViewType.DEFAULT -> {
                ClapFeedbackHolder(
                    clapCount = clapCount,
                    myClapCount = myClapCount,
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

            DetailViewType.EDIT -> {
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
            mission = Mission(
                id = 1,
                title = "앱잼 팀원 다 함께 바다 보고 오기",
                level = MissionLevel.of(1),
                isCompleted = false
            ),
            imageModel = ImageModel.Empty,
            date = "",
            content = "",
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
            viewType = DetailViewType.DEFAULT,
            title = "내 미션",
            mission = Mission(
                id = 1,
                title = "앱잼 팀원 다 함께 바다 보고 오기",
                level = MissionLevel.of(1),
                isCompleted = false
            ),
            imageModel = ImageModel.Remote(
                url = listOf("https://avatars.githubusercontent.com/u/98209004?v=4")
            ),
            date = "",
            content = "",
            writer = User(
                name = "터닝박효빈",
                profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4"
            ),
            clapCount = 10,
            myClapCount = 0,
            viewCount = 100,
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
