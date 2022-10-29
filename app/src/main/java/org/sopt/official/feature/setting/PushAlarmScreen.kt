package org.sopt.official.feature.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.sopt.official.R
import org.sopt.official.config.navigation.SettingNavGraph
import org.sopt.official.designsystem.components.CheckBox
import org.sopt.official.designsystem.components.SoptIconButton
import org.sopt.official.designsystem.style.SoptTheme
import org.sopt.official.domain.entity.Part
import org.sopt.official.feature.setting.model.PushSelectItem

@SettingNavGraph
@Destination("push_alarm")
@Composable
fun PushAlarmScreen(
    navigator: DestinationsNavigator,
    viewModel: PushAlarmConfigViewModel = hiltViewModel()
) {
    SoptTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Toolbar(
                onBack = { navigator.popBackStack() },
                onConfirm = {}
            )
            Part.values()
                .map { part ->
                    PushSelectItem(
                        part,
                        viewModel.isSelected(part)
                    ) { isChecked ->
                        viewModel.onItemCheckedChange(part, isChecked)
                    }
                }.forEach {
                    SelectBox(
                        item = it,
                        value = it.value,
                        onCheckedChange = it.onCheckedChange
                    )
                }
        }
    }
}

@Composable
private fun SelectBox(
    item: PushSelectItem,
    value: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.part.soptTitle,
                style = SoptTheme.typography.b1,
                modifier = Modifier.padding(start = 16.dp)
            )
            CheckBox(checked = value, onCheckedChange = onCheckedChange)
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = SoptTheme.colors.onSurface20,
            thickness = 1.dp
        )
    }
}

@Composable
private fun Toolbar(
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SoptIconButton(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                    onClick = onBack
                )
                Text(
                    text = "설정",
                    style = SoptTheme.typography.h5
                )
            }
            Text(
                text = "확인",
                style = SoptTheme.typography.b1,
                color = SoptTheme.colors.primary,
                modifier = Modifier
                    .clickable { onConfirm() }
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PushAlarmScreenPreview() {
    PushAlarmScreen(navigator = EmptyDestinationsNavigator)
}

@Preview(
    name = "툴바 Preview",
    showBackground = true
)
@Composable
fun ToolbarPreview() {
    SoptTheme {
        Toolbar(onBack = {}, onConfirm = {})
    }
}

@Preview(
    name = "체크박스 Checked Preview",
    showBackground = true
)
@Composable
fun CheckedCheckBoxPreview() {
    SoptTheme {
        SelectBox(
            item = PushSelectItem(Part.ANDROID),
            value = true,
            onCheckedChange = {}
        )
    }
}

@Preview(
    name = "체크박스 Unchecked Preview",
    showBackground = true
)
@Composable
fun UncheckedCheckBoxPreview() {
    SoptTheme {
        SelectBox(
            item = PushSelectItem(Part.ANDROID),
            value = false,
            onCheckedChange = {}
        )
    }
}
