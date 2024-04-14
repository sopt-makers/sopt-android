package org.sopt.official.stamp.designsystem.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme

@Composable
fun SoptampButton(text: String, modifier: Modifier = Modifier, onClicked: () -> Unit) {
    Box(
        modifier = modifier
            .noRippleClickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = text,
            style = SoptTheme.typography.sub1,
            color = SoptTheme.colors.white
        )
    }
}
