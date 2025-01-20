package org.sopt.official.feature.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.designsystem.SoptTheme

@AndroidEntryPoint
class FakeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoptTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(innerPadding)
                    ) {
                        HomeRoute()
                    }
                }
            }
        }
    }
}
