package org.sopt.official.feature.fortune

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.designsystem.SoptTheme

@AndroidEntryPoint
class FortuneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoptTheme {
                MainScreen()
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, FortuneActivity::class.java)
        }
    }
}
