package org.sopt.official.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.style.SoptTheme
import org.sopt.official.style.SoptTypography

@Composable
fun SplashScreen() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoptTheme.colors.onSurface90)
        ) {
            Text(
                text = "Hello SOPT",
                style = SoptTheme.typography.h1,
                color = SoptTheme.colors.background,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview("Splash 화면")
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
