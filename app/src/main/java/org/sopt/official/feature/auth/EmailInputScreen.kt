package org.sopt.official.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import org.sopt.official.config.navigation.AuthNavGraph
import org.sopt.official.style.SoptTheme
import org.sopt.official.style.White

@AuthNavGraph(start = true)
@Destination("email")
@Composable
fun EmailInputScreen() {
    SoptTheme {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color = White,
                darkIcons = false
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "EmailInputScreen", color = SoptTheme.colors.onBackground, style = SoptTheme.typography.h1)
        }
    }
}
