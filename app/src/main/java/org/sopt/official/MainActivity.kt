package org.sopt.official

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.feature.NavGraphs
import org.sopt.official.feature.update.InAppUpdateManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inAppUpdateManager.setOnFlexibleUpdateCompleted {
            Toast.makeText(this, "Flexible Update completed", Toast.LENGTH_SHORT).show()
        }
        lifecycle.addObserver(inAppUpdateManager)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdateManager.onActivityResult(requestCode, resultCode, data)
    }
}
