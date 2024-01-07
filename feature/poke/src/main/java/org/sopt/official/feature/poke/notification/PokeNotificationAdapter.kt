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
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeNotificationBinding
import org.sopt.official.feature.poke.databinding.ItemPokeNotificationHeaderBinding
import org.sopt.official.feature.poke.poke_user_recycler_view.PokeUserListClickListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor

class PokeNotificationAdapter(
    private val clickListener: PokeUserListClickListener,
): ListAdapter<PokeUser, PokeNotificationAdapter.NotificationListViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.userId == new.userId },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListViewHolder {
        return NotificationListViewHolder(ItemPokeNotificationBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NotificationListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    fun updatePokeUserItemPokeState(userId: Int) {
        val newList = currentList.toMutableList()
        val pokeUser = newList.find { it.userId == userId }
        val position = newList.indexOf(pokeUser)

        pokeUser?.isAlreadyPoke = true
        submitList(newList)
        notifyItemChanged(position)
    }

    fun updatePokeNotification(newList: List<PokeUser>) {
        submitList(newList)
        notifyDataSetChanged()
    }

    inner class NotificationListViewHolder(
        private val viewBinding: ItemPokeNotificationBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun onBind(item: PokeUser) {
            with(viewBinding) {
                item.profileImage.takeIf { it.isNotEmpty() }?.let {
                    imgUserProfile.load(it) { transformations(CircleCropTransformation()) }
                } ?: imgUserProfile.setImageResource(R.drawable.ic_empty_profile)
                imgUserProfileOutline.setRelationStrokeColor(item.relationName)
                tvUserName.text = item.name
                tvUserGeneration.text = "${item.generation}기 ${item.part}"
                tvUserMessage.text = item.message
                tvUserFriendsStatus.text = if (item.isFirstMeet) {
                    item.mutualRelationMessage
                } else {
                    "친한친구 ${item.pokeNum}콕"
                }
            }
        }
    }
}
