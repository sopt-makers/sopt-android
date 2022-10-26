package org.sopt.official.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.style.SoptTheme

@Composable
fun SoptTopAppBar(
    title: @Composable () -> Unit,
    contentModifier: Modifier = SoptAppBarDefault.modifierWithAppBarDefaultPadding,
    navigationIcon: (@Composable RowScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent,
    elevation: Dp = 0.dp
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .height(SoptAppBarDefault.height),
        color = backgroundColor,
        elevation = elevation
    ) {
        Row(
            modifier = contentModifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }
    }
}

object SoptAppBarDefault {
    val height = 56.dp
    val modifierWithAppBarDefaultPadding = Modifier.padding(8.dp)
}

@Preview
@Composable
fun PreviewSoptTopBar() {
    SoptTheme {
        SoptTopAppBar(title = { Text(text = "hello") })
    }
}
