package org.sopt.official.feature.mypage.signout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.R

@Composable
internal fun SignOutWithdrawInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(top = 28.dp, bottom = 34.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_withdraw),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(64.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "탈퇴 시 유의사항",
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface50
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "회원 탈퇴를 신청하시면 해당 이메일은 즉시 탈퇴 처리됩니다.\n" +
                "탈퇴 처리 시 계정 내에서 입력했던 정보는 영구적으로 삭제되며, 복구가 어렵습니다.",
            style = SoptTheme.typography.body14R,
            color = Gray60
        )
    }
}

@Preview
@Composable
private fun SignOutWithdrawInfoPreview() {
    SoptTheme {
        SignOutWithdrawInfo()
    }
}