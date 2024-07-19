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
@file:Suppress("DEPRECATION")

package org.sopt.official.feature.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.viewBinding
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.databinding.ActivityNotificationDetailBinding

@AndroidEntryPoint
class NotificationDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNotificationDetailBinding::inflate)
    private val viewModel by viewModels<NotificationDetailViewModel>()

    private val args by serializableExtra(StartArgs(""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includeAppBarBackArrow.textViewTitle.text = getString(R.string.toolbar_notification)
        viewModel.getNotificationDetail(args?.notificationId ?: "")

        initStateFlowValues()
        initClickListeners()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newArgs = intent.getSerializableExtra("args") as StartArgs
        viewModel.getNotificationDetail(newArgs.notificationId)
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
            linearLayoutLinkButton.visibility = when (
                notification.deepLink.isNullOrBlank() &&
                    notification.webLink.isNullOrBlank()
            ) {
                true -> View.GONE
                false -> View.VISIBLE
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            includeAppBarBackArrow.toolbar.setOnClickListener(clickListener)
            linearLayoutLinkButton.setOnClickListener(clickListener)
        }
    }

    private val clickListener = View.OnClickListener {
        binding.apply {
            when (it) {
                includeAppBarBackArrow.toolbar -> onBackPressed()
                linearLayoutLinkButton -> viewModel.notificationDetail.value?.let {
                    startActivity(
                        SchemeActivity.getIntent(
                            this@NotificationDetailActivity,
                            SchemeActivity.StartArgs(
                                notificationId = it.notificationId,
                                link = it.webLink ?: it.deepLink ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    data class StartArgs(
        val notificationId: String
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, NotificationDetailActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}
