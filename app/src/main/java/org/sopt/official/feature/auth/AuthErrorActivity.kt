package org.sopt.official.feature.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.designsystem.SoptTheme

@AndroidEntryPoint
class AuthErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                AuthErrorScreen()
            }
        }
    }

    @Composable
    fun AuthErrorScreen() {

    }

    @Preview(showBackground = true)
    @Composable
    fun AuthErrorPreview() {
        SoptTheme {
            AuthErrorScreen()
        }
    }
}

