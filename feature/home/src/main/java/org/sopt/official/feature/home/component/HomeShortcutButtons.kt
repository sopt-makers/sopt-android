package org.sopt.official.feature.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
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
import org.sopt.official.feature.home.R.drawable.ic_folder
import org.sopt.official.feature.home.R.drawable.ic_homepage
import org.sopt.official.feature.home.R.drawable.ic_instagram
import org.sopt.official.feature.home.R.drawable.ic_member
import org.sopt.official.feature.home.R.drawable.ic_pencil
import org.sopt.official.feature.home.R.drawable.ic_project
import org.sopt.official.feature.home.R.drawable.is_playground

@Composable
internal fun HomeShortcutButtonsForMember(
    onPlaygroundClick: () -> Unit,
    onStudyClick: () -> Unit,
    onMemberClick: () -> Unit,
    onProjectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeShortcutButton(
            icon = is_playground,
            text = "Playground",
            onClick = onPlaygroundClick,
        )
        HomeShortcutButton(
            icon = ic_pencil,
            text = "모임/스터디",
            onClick = onStudyClick,
        )
        HomeShortcutButton(
            icon = ic_member,
            text = "멤버",
            onClick = onMemberClick,
        )
        HomeShortcutButton(
            icon = ic_project,
            text = "프로젝트",
            onClick = onProjectClick,
        )
    }
}

@Preview
@Composable
private fun HomeShortcutButtonsForMemberPreview() {
    SoptTheme {
        HomeShortcutButtonsForMember(
            onPlaygroundClick = { },
            onStudyClick = { },
            onMemberClick = { },
            onProjectClick = { },
        )
    }
}

@Composable
internal fun HomeShortcutButtonsForVisitor(
    onHomePageClick: () -> Unit,
    onPlaygroundClick: () -> Unit,
    onProjectClick: () -> Unit,
    onInstagramClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeShortcutButton(
            icon = ic_homepage,
            text = "홈페이지",
            onClick = onHomePageClick,
        )
        HomeShortcutButton(
            icon = is_playground,
            text = "활동후기",
            onClick = onPlaygroundClick,
        )
        HomeShortcutButton(
            icon = ic_folder,
            text = "프로젝트",
            onClick = onProjectClick,
        )
        HomeShortcutButton(
            icon = ic_instagram,
            text = "인스타그램",
            onClick = onInstagramClick,
        )
    }
}

@Preview
@Composable
private fun HomeShortcutButtonsForVisitorPreview() {
    SoptTheme {
        HomeShortcutButtonsForVisitor(
            onHomePageClick = { },
            onPlaygroundClick = { },
            onProjectClick = { },
            onInstagramClick = { },
        )
    }
}

@Composable
private fun HomeShortcutButton(
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable { onClick() },
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
