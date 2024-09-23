package org.sopt.official.feature.fortune.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun FortuneTopBar(
    modifier: Modifier = Modifier,
    onClickNavigationIcon: () -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
                .padding(8.dp)
                .clickable(onClick = onClickNavigationIcon),
            tint = SoptTheme.colors.onBackground
        )
    }
}

@Preview
@Composable
fun FortuneTopBarPreview() {
    SoptTheme {
        FortuneTopBar {

        }
    }
}
