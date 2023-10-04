package org.sopt.official.feature.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val notificationHistoryLayoutManager
        get() = binding.recyclerViewNotificationHistory.layoutManager as LinearLayoutManager

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
        binding.recyclerViewNotificationHistory.apply {
            adapter = NotificationHistoryListViewAdapter(this@NotificationHistoryActivity)
            addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = notificationHistoryLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = notificationHistoryLayoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 10 == 0) {
                viewModel.getNotificationHistory()
            }
        }
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
                notificationHistoryAdapter?.updateNotificationHistoryList(it)
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