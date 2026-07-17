package org.sopt.official.feature.mypage.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.R
import org.sopt.official.model.UserStatus

@Composable
internal fun MyPageUserContentsInfo(
    userStatus: UserStatus,
    modifier: Modifier = Modifier,
    isAppjamPeriod : Boolean = false,
    isAppjamJoined: Boolean = false,
    totalSoptampCount: Int? = 0,
    totalPokeCount: Int? = 0,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        val showSoptampLog = userStatus == UserStatus.ACTIVE || (isAppjamPeriod && isAppjamJoined)
        if (showSoptampLog) {
            MyPageUserContentsInfoItem(
                icon = R.drawable.ic_mypage_soptamp,
                infoTitleText = "솝탬프",
                infoContentText = totalSoptampCount.toString()
            )
        }

        MyPageUserContentsInfoItem(
            icon = R.drawable.ic_mypage_poke,
            infoTitleText = "콕찌르기",
            infoContentText = totalPokeCount.toString()
        )
    }
}

@Composable
private fun MyPageUserContentsInfoItem(
    @DrawableRes icon: Int ,
    infoTitleText: String,
    infoContentText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(39.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = infoTitleText,
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface200
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${infoContentText}회",
            style = SoptTheme.typography.heading16B,
            color = White
        )
    }
}

@Preview
@Composable
private fun MyPageUserContentsInfoPreview() {
    SoptTheme {
        MyPageUserContentsInfo(
            userStatus = UserStatus.ACTIVE
        )
    }
}
