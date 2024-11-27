package org.sopt.official.feature.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SuitMedium
import org.sopt.official.feature.home.R.drawable.ic_setting

@Composable
internal fun HomeShortcutButtons(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeShortcutButton(
            icon = ic_setting,
            text = "Playground",
        )
        HomeShortcutButton(
            icon = ic_setting,
            text = "모임/스터디",
        )
        HomeShortcutButton(
            icon = ic_setting,
            text = "멤버",
        )
        HomeShortcutButton(
            icon = ic_setting,
            text = "프로젝트",
        )
    }
}

@Composable
private fun HomeShortcutButton(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(horizontal = 3.dp)) {
            HomeBox(
                modifier = Modifier.size(size = 68.dp),
                contentAlignment = Center,
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null,
                        tint = Unspecified,
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitMedium, fontSize = 14.sp, lineHeight = 20.sp
            ),
            color = colors.onSurface200,
        )
    }
}

@Preview
@Composable
private fun HomeShortcutButtonsPreview() {
    SoptTheme {
        HomeShortcutButtons()
    }
}
