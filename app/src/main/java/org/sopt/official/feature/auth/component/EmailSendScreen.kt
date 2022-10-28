package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.sopt.official.config.navigation.AuthNavGraph
import org.sopt.official.designsystem.style.SoptTheme

@AuthNavGraph
@Destination("send")
@Composable
fun EmailSendScreen(
    navigator: DestinationsNavigator,
) {
    SoptTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(108.dp))
            AuthHeader(
                containerModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                emphasizedTitleLabel = "인증 메일",
                extraTitleLabel = "을 발송했어요",
                contentLabel = "메일 내에서 \'회원가입 계속하기\' 버튼을 눌러\n" +
                        "인증 절차를 진행해주세요."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailSendScreenPreview() {
    EmailSendScreen(navigator = EmptyDestinationsNavigator)
}
