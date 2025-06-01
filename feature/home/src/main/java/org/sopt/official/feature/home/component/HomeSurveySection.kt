package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R

@Composable
internal fun HomeSurveySection(
    surveyTitle: String,
    surveyDescription: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_survey),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Text(
            text = surveyTitle,
            style = SoptTheme.typography.heading20B,
            color = SoptTheme.colors.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text = surveyDescription,
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Text(
            text = buttonText,
            style = SoptTheme.typography.title14SB,
            color = SoptTheme.colors.onSurface,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Orange400)
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }

}
