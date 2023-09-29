package org.sopt.official.feature.notification

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
import org.sopt.official.data.model.notification.response.NotificationHistoryItem
import org.sopt.official.databinding.ActivityNotificationHistoryBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class NotificationHistoryActivity : AppCompatActivity(), NotificationHistoryItemClickListener {

    private val binding by viewBinding(ActivityNotificationHistoryBinding::inflate)
    private val viewModel by viewModels<NotificationHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        initClickListeners()
        initStateFlowValues()

        viewModel.getNotificationHistory(0)
    }

    private fun initToolbar() {
        binding.includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
    }

    private fun initRecyclerView() {
        binding.recyclerViewNotificationHistory.adapter = NotificationHistoryRecyclerViewAdapter(viewModel.notificationHistoryList.value.list, this)
    }

    private fun initClickListeners() {
        binding.apply {
            includeAppBarBackArrow.toolbar.setOnClickListener(clickListener)
            textViewReadAll.setOnClickListener(clickListener)
        }
    }

    private val clickListener = View.OnClickListener {
        binding.apply {
            when (it) {
                includeAppBarBackArrow.toolbar -> onBackPressedDispatcher.onBackPressed()
                textViewReadAll -> viewModel.updateEntireNotificationReadingState()
            }
        }
    }

    private fun initStateFlowValues() {
        viewModel.notificationHistoryList.flowWithLifecycle(lifecycle)
            .onEach { setTextViewReadAllVisibility(it.list.isNotEmpty()) }
            .launchIn(lifecycleScope)

        viewModel.updateEntireNotificationReadingState.flowWithLifecycle(lifecycle)
            .onEach {

            }.launchIn(lifecycleScope)
    }

    private fun setTextViewReadAllVisibility(isVisible: Boolean) {
        binding.textViewReadAll.visibility = when (isVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun onClickNotificationHistoryItem(item: NotificationHistoryItem) {
        when (item.type) {

        }
    }
}