package org.sopt.official.feature.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.R
import org.sopt.official.config.messaging.RemoteMessageType
import org.sopt.official.databinding.ActivityNotificationDetailBinding
import org.sopt.official.feature.notification.NotificationHistoryActivity.Companion.CONTENT
import org.sopt.official.feature.notification.NotificationHistoryActivity.Companion.ID
import org.sopt.official.feature.notification.NotificationHistoryActivity.Companion.TITLE
import org.sopt.official.feature.notification.NotificationHistoryActivity.Companion.CATEGORY
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class NotificationDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNotificationDetailBinding::inflate)
    private val viewModel by viewModels<NotificationDetailViewModel>()

    private val id: Int by lazy {
        intent.getIntExtra(ID, 0)
    }
    private val title: String? by lazy {
        intent.getStringExtra(TITLE)
    }
    private val content: String? by lazy {
        intent.getStringExtra(CONTENT)
    }
    private val type: String? by lazy {
        intent.getStringExtra(CATEGORY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (id != 0) viewModel.updateNotificationReadingState(id)
        initNotificationDetail()
        initClickListeners()
    }

    private fun initNotificationDetail() {
        binding.apply {
            includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
            textViewNotificationTitle.text = title
            textViewNotificationContent.text = content
            linearLayoutNewsDetailButton.visibility = when (type) {
                RemoteMessageType.NOTICE.name -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            includeAppBarBackArrow.toolbar.setOnClickListener(clickListener)
            linearLayoutNewsDetailButton.setOnClickListener(clickListener)
        }
    }

    private val clickListener = View.OnClickListener {
        binding.apply {
            when (it) {
                includeAppBarBackArrow.toolbar -> onBackPressed()
                linearLayoutNewsDetailButton -> {} // TODO: Set intent to link.
            }
        }
    }
}