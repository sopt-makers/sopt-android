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
package org.sopt.official.feature.poke.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.domain.poke.entity.PokeNotificationItem
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeNotificationBinding
import org.sopt.official.feature.poke.databinding.ItemPokeNotificationHeaderBinding

class PokeNotificationAdapter: ListAdapter<PokeNotificationItem, RecyclerView.ViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.userId == new.userId },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    override fun getItemViewType(position: Int) = when (position) {
        0 -> R.layout.item_poke_notification_header
        else -> R.layout.item_poke_notification
    }

    override fun getItemCount(): Int = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_poke_notification_header -> NotificationHeaderViewHolder(ItemPokeNotificationHeaderBinding.inflate(LayoutInflater.from(parent.context)))
            else -> NotificationListViewHolder(ItemPokeNotificationBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationHeaderViewHolder) {
            holder.bind()
        }

        if (holder is NotificationListViewHolder) {
            currentList[position].let { item ->
                holder.bind(item)
            }
        }
    }

    fun updatePokeNotification(newList: List<PokeNotificationItem>) {
        submitList(newList)
        notifyDataSetChanged()
    }

    inner class NotificationHeaderViewHolder(
        viewBinding: ItemPokeNotificationHeaderBinding
    ): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind() {}
    }

    inner class NotificationListViewHolder(
        private val viewBinding: ItemPokeNotificationBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: PokeNotificationItem) {
            with(viewBinding) {
                item.profileImage.takeIf { it.isNotEmpty() }?.let {
                    imgUserProfile.load(it) { transformations(CircleCropTransformation()) }
                } ?: imgUserProfile.setImageResource(R.drawable.ic_empty_profile)
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
