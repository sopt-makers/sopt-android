package org.sopt.official.feature.appjamtamp.missionlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.appjamtamp.R

@Composable
internal fun AppjamtampFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box (
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        AppjamtampFloatingBody()

        AppjamtampBadge(
            text = "New",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
                .offset(y = (-9).dp)
        )
    }
}

@Composable
private fun AppjamtampFloatingBody() {
    Row (
        modifier = Modifier
            .background(
                color = White,
                shape = RoundedCornerShape(46.dp)
            )
            .padding(vertical = 12.dp)
            .padding(start = 9.dp, end = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_trophy),
            contentDescription = null
        )

        Text(
            text = "앱잼팀 랭킹",
            style = SoptTheme.typography.heading18B,
            color = Gray950
        )
    }
}

@Preview
@Composable
private fun AppjamtampFloatingButtonPreview() {
    SoptTheme {
        AppjamtampFloatingButton()
    }
}