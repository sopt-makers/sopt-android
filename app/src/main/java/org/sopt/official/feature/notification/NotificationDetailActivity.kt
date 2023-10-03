package org.sopt.official.feature.notification

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.databinding.ActivityNotificationDetailBinding
import org.sopt.official.feature.notification.NotificationHistoryActivity.Companion.NOTIFICATION_ID
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class NotificationDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNotificationDetailBinding::inflate)
    private val viewModel by viewModels<NotificationDetailViewModel>()

    private val notificationId: Int by lazy {
        intent.getIntExtra(NOTIFICATION_ID, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
        viewModel.getNotificationDetail(notificationId)

        initStateFlowValues()
        initClickListeners()
    }

    private fun initStateFlowValues() {
        viewModel.apply {
            notificationDetail.flowWithLifecycle(lifecycle)
                .onEach { it?.let { notification -> initNotificationDetail(notification) } }
                .launchIn(lifecycleScope)
        }
    }

    private fun initNotificationDetail(notification: NotificationDetailResponse) {
        binding.apply {
            textViewNotificationTitle.text = notification.title
            textViewNotificationContent.text = notification.content
            linearLayoutNewsDetailButton.visibility = when (
                notification.deepLink.isNullOrBlank()
                && notification.webLink.isNullOrBlank()
            ) {
                true -> View.GONE
                false -> View.VISIBLE
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
                linearLayoutNewsDetailButton -> viewModel.notificationDetail.value?.let {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.webLink)))
                }
            }
        }
    }
}