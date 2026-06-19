package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R

@Composable
fun SopletterExplainArea (
    modifier : Modifier = Modifier,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SoptTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Image(
            painter = painterResource(id = R.drawable.img_sopletter_logo),
            contentDescription = null,
            contentScale = ContentScale.None,
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = stringResource(id = R.string.sopletter_write_explain),
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface10,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterExplainAreaPreview() {
    SoptTheme {
        SopletterExplainArea()
    }
}