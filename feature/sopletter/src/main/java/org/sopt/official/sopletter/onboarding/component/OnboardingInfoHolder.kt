package org.sopt.official.sopletter.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R

@Composable
internal fun OnboardingInfoHolder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.img_sopletter_mailbox),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.img_sopletter_onboarding_title),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "우리 기수 회원들에게 하고 싶은 말을 남겨보세요.\n" +
                "익명으로 부담없이 마음을 전할 수 있어요.\n" +
                "추억을 남기고, 우리 기수만의 기록을 쌓아보세요.",
            style = SoptTheme.typography.body16M,
            color = SoptTheme.colors.onSurface200,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun OnboardingInfoHolderPreview() {
    SoptTheme {
        OnboardingInfoHolder()
    }
}