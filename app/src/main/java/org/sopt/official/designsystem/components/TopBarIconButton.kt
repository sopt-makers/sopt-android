package org.sopt.official.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.style.SoptTheme

@Composable
fun TopBarIconButton() {
    Row(
        modifier = Modifier.clickable { /*TODO*/ }
    ) {
        Image(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back button of Notice detail screen"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBarIconButton() {
    SoptTheme {
        TopBarIconButton()
    }
}
