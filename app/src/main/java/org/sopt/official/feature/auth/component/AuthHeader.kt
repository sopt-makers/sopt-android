package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.style.SoptTheme

@Composable
fun AuthHeader(
    containerModifier: Modifier = Modifier,
    emphasizedTitleLabel: String,
    extraTitleLabel: String,
    contentLabel: String
) {
    Column(
        modifier = containerModifier
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = SoptTheme.colors.primary)) {
                    append(emphasizedTitleLabel)
                }
                withStyle(style = SpanStyle(color = SoptTheme.colors.onSurface90)) {
                    append(extraTitleLabel)
                }
            },
            style = SoptTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contentLabel,
            style = SoptTheme.typography.b1,
            color = SoptTheme.colors.onSurface50
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthHeaderPreview() {
    SoptTheme {
        AuthHeader(
            containerModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            emphasizedTitleLabel = "이메일",
            extraTitleLabel = "을 입력해주세요",
            contentLabel = "SOPT 지원 시 사용했던 이메일을 입력하면\n" + "회원인증을 할 수 있어요"
        )
    }
}
