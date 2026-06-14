package org.sopt.official.sopletter.name.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R
import org.sopt.official.sopletter.name.NameState

@Composable
internal fun SopletterNameInfoHolder(
    info: NameState,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_sopletter_name_letter),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${info.generation}기 솝레터에 입장할 준비 되셨나요?\n" +
                "솝레터는 100% 익명이에요.",
            style = SoptTheme.typography.title18SB,
            color = SoptTheme.colors.onSurface50,
            modifier = Modifier
                .padding(horizontal = 30.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = SoptTheme.colors.onSurface600,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "나의 닉네임은",
            style = SoptTheme.typography.title20SB,
            color = SoptTheme.colors.onSurface300,
        )

        Text(
            text = info.name,
            style = SoptTheme.typography.title24SB,
            color = SoptTheme.colors.onSurface30,
        )

        Spacer(modifier = Modifier.height(34.dp))
    }
}

@Preview
@Composable
private fun NameInfoHolderPreview() {
    SoptTheme {
        SopletterNameInfoHolder(
            info = NameState(
                name = "익명의 김솝트",
                generation = 37
            )
        )
    }
}