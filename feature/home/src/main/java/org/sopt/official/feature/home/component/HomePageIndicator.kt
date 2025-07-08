package org.sopt.official.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun HomePageIndicator(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = SoptTheme.colors.onSurface50,
    defaultColor: Color = SoptTheme.colors.onSurface700,
    animationDurationInMillis: Int = 300,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        (0 until numberOfPages).forEach { page ->
            PageIndicatorItem(
                isSelected = page == selectedPage % numberOfPages,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                animationDurationInMillis = animationDurationInMillis
            )
        }
    }
}

@Composable
private fun PageIndicatorItem(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis
        ),
        label = "color"
    )

    Box(
        modifier = Modifier
            .size(width = 16.dp, height = 4.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomePageIndicatorPreview() {
    SoptTheme {
        var selectedPage by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(3000L)
                selectedPage += 1
            }
        }

        HomePageIndicator(
            numberOfPages = 5,
            selectedPage = selectedPage
        )
    }
}
