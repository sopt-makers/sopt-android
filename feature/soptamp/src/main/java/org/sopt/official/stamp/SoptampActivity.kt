package org.sopt.official.stamp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.stamp.feature.NavGraphs
import javax.inject.Inject

val LocalTracker = staticCompositionLocalOf<AmplitudeTracker> {
    error("No AmplitudeTracker provided")
}

@AndroidEntryPoint
class SoptampActivity : AppCompatActivity() {
    @Inject
    lateinit var tracker: AmplitudeTracker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.White,
                    darkIcons = true
                )
            }
            CompositionLocalProvider(LocalTracker provides tracker) {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
