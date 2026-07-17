/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
