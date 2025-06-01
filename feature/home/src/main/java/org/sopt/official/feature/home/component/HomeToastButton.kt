package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.MdsGray950
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.Orange500
import org.sopt.official.designsystem.Orange700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.designsystem.component.UrlImage

@Composable
fun HomeToastButton(
    imageUrl: String,
    longTitle: String,
    missionDescription: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Orange400)
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UrlImage(
            url = imageUrl,
            modifier = Modifier
                .size(42.dp)
                .background(color = Orange500, shape = CircleShape)
                .padding(6.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(
                start = 7.dp,
                end = 4.dp
            )
        ) {
            Text(
                text = longTitle,
                style = SoptTheme.typography.heading16B,
                color = MdsGray950,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                text = missionDescription,
                style = SoptTheme.typography.label12SB,
                color = Orange700,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buttonText,
            style = SoptTheme.typography.body13M,
            color = White,
            modifier = Modifier
                .clip(CircleShape)
                .background(Black)
                .padding(horizontal = 11.dp, vertical = 6.dp)
                .clickable(onClick = onClick)
        )
    }
}
