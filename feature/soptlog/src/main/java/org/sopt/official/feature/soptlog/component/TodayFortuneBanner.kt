package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.R

@Composable
fun TodayFortuneBanner(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SoptTheme.colors.onSurface800)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_fortune_title),
            modifier = Modifier.size(60.dp),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                style = SoptTheme.typography.heading18B,
                color = SoptTheme.colors.surface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "바로 확인하기 >",
                style = SoptTheme.typography.label12SB,
                color = SoptTheme.colors.onSurface200,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayFortuneBannerPreview() {
    SoptTheme {
        TodayFortuneBanner(
            title = "차은우님, 잊지 말아야 할 말을 듣게 될것 같아요오오오?",
            onClick = {}
        )
    }
}
