package org.sopt.official.feature.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.databinding.ActivityNotificationHistoryBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class NotificationHistoryActivity : AppCompatActivity(), NotificationHistoryItemClickListener {

    private val binding by viewBinding(ActivityNotificationHistoryBinding::inflate)
    private val viewModel by viewModels<NotificationHistoryViewModel>()

    private val notificationHistoryAdapter
        get() = binding.recyclerViewNotificationHistory.adapter as NotificationHistoryListViewAdapter?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        initClickListeners()
        initStateFlowValues()
    }

    private fun initToolbar() {
        binding.includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
    }

    private fun initRecyclerView() {
        binding.recyclerViewNotificationHistory.adapter = NotificationHistoryListViewAdapter(this@NotificationHistoryActivity)
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
                textViewReadAll -> {
                    viewModel.updateEntireNotificationReadingState()
                    notificationHistoryAdapter?.updateEntireNotificationReadingState()
                }
            }
        }
    }

    private fun initStateFlowValues() {
        lifecycleScope.launch {
            viewModel.notificationHistoryList.collectLatest {
                setEmptyViewVisibility(it.isEmpty())
                setTextViewReadAllVisibility(it.isNotEmpty())
                notificationHistoryAdapter?.submitList(it)
            }
        }
    }

    private fun setEmptyViewVisibility(isVisible: Boolean) {
        binding.includeNotificationHistoryEmptyView.root.visibility = when (isVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    private fun setTextViewReadAllVisibility(isVisible: Boolean) {
        binding.textViewReadAll.visibility = when (isVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun onClickNotificationHistoryItem(position: Int) {
        notificationHistoryAdapter?.updateNotificationReadingState(position)
        val clickedNotification = viewModel.notificationHistoryList.value[position]

        Intent(this, NotificationDetailActivity::class.java).run {
            putExtra(NOTIFICATION_ID, clickedNotification.notificationId)
            startActivity(this)
        }
    }

    companion object {
        const val NOTIFICATION_ID = "notificationId"
    }
}