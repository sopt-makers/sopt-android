package org.sopt.official.feature.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.feature.update.InAppUpdateManager
import org.sopt.official.feature.update.InAppUpdateManagerFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: InAppUpdateManagerFactory
    lateinit var inAppUpdateManager: InAppUpdateManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inAppUpdateManager = factory.create(this)
        inAppUpdateManager.setOnFlexibleUpdateCompleted {
            Toast.makeText(this, "Flexible Update completed", Toast.LENGTH_SHORT).show()
        }
        lifecycle.addObserver(inAppUpdateManager)
        setContent {
            Text(text = "Hello World!")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdateManager.onActivityResult(requestCode, resultCode, data)
    }
}
