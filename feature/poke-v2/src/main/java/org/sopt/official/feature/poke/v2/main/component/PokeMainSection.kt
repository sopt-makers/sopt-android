package org.sopt.official.feature.poke.v2.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeMainSection(
    title: String,
    onTitleClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoptTheme.colors.bg.layer.default)
    ) {
        PokeMainTitle(
            title = title,
            onTitleClick = onTitleClick
        )

        content()
    }
}

@Composable
private fun PokeMainTitle(
    title: String,
    onTitleClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTitleClick)
            .padding(start = 12.dp)
    ) {
        Text(
            text = title,
            color = SoptTheme.colors.fg.neutral.bold,
            style = SoptTheme.typography.title5
        )

        Icon(
            imageVector = ImageVector.vectorResource(MdsIcons.chevronRightOutlined),
            contentDescription = null,
            tint = SoptTheme.colors.fg.neutral.subtle,
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
        )
    }
}
