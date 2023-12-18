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
package org.sopt.official.feature.poke

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.R
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.databinding.ItemPokeNotificationBinding
import org.sopt.official.domain.entity.poke.PokeNotificationHistoryItem

class PokeNotificationAdapter(
//    private val clickListener: NotificationHistoryItemClickListener
) : ListAdapter<PokeNotificationHistoryItem, PokeNotificationAdapter.ViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.userId == new.userId },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    private var _viewBinding: ItemPokeNotificationBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun getItemViewType(position: Int) = when (position) {
        0 -> R.layout.item_poke_notification_header
        else -> R.layout.item_poke_notification
    }

    override fun getItemCount(): Int = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _viewBinding = ItemPokeNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].let { item ->
            holder.bind(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        _viewBinding = null
    }

//    fun updateNotificationReadingState(position: Int) {
//        val newList = currentList.toMutableList()
//        newList[position].isRead = true
//        submitList(newList)
//        notifyItemChanged(position)
//    }
//
//    fun updateEntireNotificationReadingState() {
//        val newList = currentList.toMutableList()
//        for (notification in newList) {
//            notification.isRead = true
//        }
//        submitList(newList)
//        notifyItemRangeChanged(0, newList.size)
//    }

    fun updatePokeNotification(newList: List<PokeNotificationHistoryItem>) {
        submitList(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val viewBinding: ItemPokeNotificationBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: PokeNotificationHistoryItem) {
            with(viewBinding) {
                tvUserName.text = item.name
                tvUserGeneration.text = "${item.generation}기 ${item.part}"
                tvUserMessage.text = item.message
                tvUserFriendsStatus.text = if (item.isFirstMeet) {
                    "${item.mutual.first()} 외 ${item.mutual.size - 1}명과 친구"
                } else {
                    "친한친구 ${item.pokeNum}콕"
                }

            }
        }
    }
}
