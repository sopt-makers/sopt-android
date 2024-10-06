package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray900
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R.drawable.ic_alert

@Composable
internal fun PokeSnackBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Gray10,
                shape = RoundedCornerShape(18.dp),
            )
    ) {
        Spacer(modifier = Modifier.width(width = 16.dp))
        Image(
            imageVector = ImageVector.vectorResource(ic_alert),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(
            text = "익명 해제 시, 상대방이 나를 알 수 있어요.",
            style = SoptTheme.typography.title14SB,
            color = Gray900,
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun PokeSnackBarPreview() {
    SoptTheme {
        PokeSnackBar()
    }
}
