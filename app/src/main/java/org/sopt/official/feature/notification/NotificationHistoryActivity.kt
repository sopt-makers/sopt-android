package org.sopt.official.feature.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.view.clicks
import org.sopt.official.R
import org.sopt.official.databinding.ActivityNotificationHistoryBinding
import org.sopt.official.databinding.ActivitySoptMainBinding
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.feature.main.MainViewModel
import org.sopt.official.util.rx.observeOnMain
import org.sopt.official.util.serializableExtra
import org.sopt.official.util.ui.throttleUi
import org.sopt.official.util.viewBinding

class NotificationHistoryActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNotificationHistoryBinding::inflate)
//    private val viewModel by viewModels<MainViewModel>()
//    private val args by serializableExtra(MainActivity.StartArgs(UserStatus.UNAUTHENTICATED))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initClickListeners()
    }

    private fun initToolbar() {
        binding.includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
    }

    private fun initClickListeners() {
        binding.apply {
            includeAppBarBackArrow.toolbar.setOnClickListener(clickListener)
        }
    }

    private val clickListener = View.OnClickListener {
        binding.apply {
            when (it) {
                includeAppBarBackArrow.toolbar -> onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}