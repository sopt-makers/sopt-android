package org.sopt.official.feature.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.sopt.official.R
import org.sopt.official.databinding.ActivityNotificationDetailBinding
import org.sopt.official.databinding.ActivityNotificationHistoryBinding
import org.sopt.official.util.viewBinding

class NotificationDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNotificationDetailBinding::inflate)
//    private val viewModel by viewModels<MainViewModel>()
//    private val args by serializableExtra(MainActivity.StartArgs(UserStatus.UNAUTHENTICATED))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}