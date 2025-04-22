package org.sopt.official.stamp.designsystem.component.mission

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.Orange300

@Composable
fun SpecialMissionTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.level_star),
            contentDescription = "Star Of Mission Level",
            tint = Orange300
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_text_close),
            contentDescription = null,
            tint = White
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "10",
            style = SoptTheme.typography.body14M,
            color = White
        )
        Spacer(modifier = Modifier.width(7.dp))
        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(7.dp),
            color = Gray600
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = "특별미션",
            style = SoptTheme.typography.body14M,
            color = Orange300
        )
    }
}
