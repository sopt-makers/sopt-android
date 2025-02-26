package org.sopt.official.feature.home.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SuitMedium
import org.sopt.official.designsystem.SuitSemiBold
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel

@Composable
internal fun HomeGenerationChips(
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    modifier: Modifier = Modifier,
) {
    val recentChipColor = if (homeUserSoptLogDashboardModel.isActivated) Orange400 else Black40
    val recentChipTextColor = if (homeUserSoptLogDashboardModel.isActivated) colors.background else colors.onBackground

    Row(modifier = modifier) {
        RecentGenerationChip(
            chipColor = recentChipColor,
            textColor = recentChipTextColor,
            text = homeUserSoptLogDashboardModel.recentGenerationDescription,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        LastGenerationChips(homeUserSoptLogDashboardModel.lastGenerations)
    }
}

@Composable
internal fun RecentGenerationChip(
    @ColorRes chipColor: Color,
    @ColorRes textColor: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier.background(
            color = chipColor,
            shape = RoundedCornerShape(size = 15.dp),
        ).size(
            width = 82.dp,
            height = 24.dp,
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitSemiBold, fontSize = 12.sp, lineHeight = 15.sp
            ),
            color = textColor,
        )
    }
}

@Composable
private fun LastGenerationChips(generations: ImmutableList<Long>) {
    generations.forEachIndexed { index, generation ->
        when (index) {
            0 -> GenerationChip(
                chipColor = colors.onSurface600,
                textColor = colors.onBackground,
                text = generation.toString(),
            )

            1 -> GenerationChip(
                chipColor = colors.onSurface700,
                textColor = colors.onSurface10,
                text = generation.toString(),
            )

            2 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface100,
                text = generation.toString(),
            )

            3 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface200,
                text = generation.toString(),
            )

            4 -> GenerationChip(
                chipColor = Transparent,
                textColor = colors.onSurface300,
                text = generation.toString(),
            )

            5 -> {
                GenerationChip(
                    chipColor = Transparent,
                    textColor = colors.onBackground,
                    text = "+1",
                )
                return@forEachIndexed
            }
        }
        Spacer(modifier = Modifier.width(width = 4.dp))
    }
}

@Composable
private fun GenerationChip(
    @ColorRes chipColor: Color,
    @ColorRes textColor: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier.background(
            color = chipColor,
            shape = CircleShape,
        ).size(size = 24.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitMedium, fontSize = 12.sp, lineHeight = 15.sp
            ),
            color = textColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGenerationChipsPreview() {
    SoptTheme {
        HomeGenerationChips(
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel()
        )
    }
}
