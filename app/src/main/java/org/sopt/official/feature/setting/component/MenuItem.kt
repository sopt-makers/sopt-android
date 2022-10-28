package org.sopt.official.feature.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.style.SoptTheme
import org.sopt.official.feature.setting.model.SettingItem

@Composable
fun MenuItem(
    item: SettingItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = SoptTheme.typography.b1
            )
            Image(
                painter = painterResource(id = item.rightIcon),
                contentDescription = item.contentDescription,
                modifier = Modifier.clickable { item.onClick() }
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = SoptTheme.colors.onSurface20,
            thickness = 1.dp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MenuItemPreview() {
    SoptTheme {
        MenuItem(
            item = SettingItem(
                title = "알림 설정",
                rightIcon = R.drawable.ic_right,
                onClick = {}
            )
        )
    }

}
