package org.sopt.official.feature.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun CertificationSnackBar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Gray10)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_auth_certification_notice),
            contentDescription = "인증 확인 아이콘"
        )
        Text(
            text = "인증번호가 전송되었어요.",
            style = SoptTheme.typography.title14SB
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AuthSnackBarPreview() {
    SoptTheme {
        CertificationSnackBar()
    }
}