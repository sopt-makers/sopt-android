package org.sopt.official.feature.appjamtamp.missiondetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.component.AppjamtampButton
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.missiondetail.component.DataPickerBottomSheet
import org.sopt.official.feature.appjamtamp.missiondetail.component.DatePicker
import org.sopt.official.feature.appjamtamp.missiondetail.component.ImageContent
import org.sopt.official.feature.appjamtamp.missiondetail.component.Memo
import org.sopt.official.feature.appjamtamp.missiondetail.component.MissionHeader
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
internal fun MissionDetailRoute() {
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
