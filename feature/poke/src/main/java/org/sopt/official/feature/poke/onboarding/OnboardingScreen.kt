@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package org.sopt.official.feature.poke.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.poke.R

@Composable
fun OnboardingScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(SoptTheme.colors.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "콕 찌르기",
                        style = SoptTheme.typography.h6,
                        color = SoptTheme.colors.onSurface
                    )
                },
                navigationIcon = {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_close),
                        contentDescription = "Close Screen"
                    )
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(SoptTheme.colors.background)
        ) {

        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    SoptTheme {
        OnboardingScreen()
    }
}
