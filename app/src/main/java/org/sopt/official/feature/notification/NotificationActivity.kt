/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.notification

import android.content.Context
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
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivityNotificationBinding

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
  private val binding by viewBinding(ActivityNotificationBinding::inflate)
  private val viewModel by viewModels<NotificationHistoryViewModel>()

  private val notificationHistoryAdapter
    get() = binding.recyclerViewNotificationHistory.adapter as NotificationListAdapter?

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
      adapter = NotificationListAdapter(notificationItemClickListener)
      addOnScrollListener(scrollListener)
    }
  }

  private val notificationItemClickListener = NotificationItemClickListener { position ->
    notificationHistoryAdapter?.updateNotificationReadingState(position)
    val notificationId = viewModel.notificationHistoryList.value[position].notificationId

    startActivity(
      NotificationDetailActivity.getIntent(
        this, NotificationDetailActivity.StartArgs(notificationId)
      )
    )
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
    fun newInstance(context: Context) = Intent(context, NotificationActivity::class.java)
  }
}
