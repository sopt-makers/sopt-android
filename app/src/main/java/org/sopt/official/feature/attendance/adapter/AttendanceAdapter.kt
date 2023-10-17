/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.attendance.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo

class AttendanceAdapter : ListAdapter<AttendanceLog, RecyclerView.ViewHolder>(diffCallback) {
    private var userInfo: AttendanceUserInfo? = null
    private var summary: AttendanceSummary? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_INFO -> UserInfoViewHolder.create(parent)
            SUMMARY -> SummaryViewHolder.create(parent)
            LOG_HEADER -> LogHeaderViewHolder.create(parent)
            LOG -> LogViewHolder.create(parent)
            else -> throw IllegalArgumentException("Illegal viewType argument: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserInfoViewHolder -> userInfo?.let { holder.onBind(it) }
            is SummaryViewHolder -> summary?.let { holder.onBind(it) }
            is LogHeaderViewHolder -> {}
            is LogViewHolder -> holder.onBind(currentList[position])
            else -> throw IllegalArgumentException("Illegal holder argument: ${holder::class.java.simpleName}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> USER_INFO
            1 -> SUMMARY
            2 -> LOG_HEADER
            else -> LOG
        }
    }

    fun updateUserInfo(userInfo: AttendanceUserInfo) {
        this.userInfo = userInfo
        notifyItemChanged(USER_INFO)
    }

    fun updateSummary(summary: AttendanceSummary) {
        this.summary = summary
        notifyItemChanged(SUMMARY)
    }

    companion object {
        private const val USER_INFO = 0
        private const val SUMMARY = 1
        private const val LOG_HEADER = 2
        private const val LOG = 3

        private val diffCallback = object : DiffUtil.ItemCallback<AttendanceLog>() {
            override fun areItemsTheSame(oldItem: AttendanceLog, newItem: AttendanceLog): Boolean {
                return oldItem.eventName == newItem.eventName
            }

            override fun areContentsTheSame(oldItem: AttendanceLog, newItem: AttendanceLog): Boolean {
                return oldItem == newItem
            }
        }
    }
}
