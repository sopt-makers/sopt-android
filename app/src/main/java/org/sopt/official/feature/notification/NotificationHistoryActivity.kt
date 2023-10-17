/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.feature.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
class NotificationHistoryActivity : AppCompatActivity() {

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
            adapter = NotificationHistoryListViewAdapter(notificationHistoryItemClickListener)
            addOnScrollListener(scrollListener)
        }
    }

    private val notificationHistoryItemClickListener = NotificationHistoryItemClickListener { position ->
        notificationHistoryAdapter?.updateNotificationReadingState(position)
        val clickedNotification = viewModel.notificationHistoryList.value[position]

        Intent(this@NotificationHistoryActivity, NotificationDetailActivity::class.java).run {
            putExtra(NOTIFICATION_ID, clickedNotification.notificationId)
            startActivity(this)
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
                binding.textViewReadAll.isVisible = it.isNotEmpty()
                binding.includeNotificationHistoryEmptyView.root.isVisible = it.isEmpty()
                notificationHistoryAdapter?.updateNotificationHistoryList(it)
            }
        }
    }

    companion object {
        const val NOTIFICATION_ID = "notificationId"
    }
}
