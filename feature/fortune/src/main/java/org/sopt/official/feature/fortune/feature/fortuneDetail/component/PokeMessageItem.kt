package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun PokeMessageItem(
    message: String,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) Gray700 else Gray800,
                shape = RoundedCornerShape(size = 6.dp),
            )
            .clickable(
                indication = null,
                interactionSource = null,
            ) { onItemClick() },
    ) {
        Text(
            text = message,
            style = SoptTheme.typography.body16M,
            color = Gray10,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokeMessageItemPreview() {
    SoptTheme {
        PokeMessageItem(
            message = "123",
            isSelected = true,
            onItemClick = { },
        )
    }
}

