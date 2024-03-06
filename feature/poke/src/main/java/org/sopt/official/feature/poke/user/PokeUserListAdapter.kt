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
package org.sopt.official.feature.poke.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeUserLargeBinding
import org.sopt.official.feature.poke.databinding.ItemPokeUserSmallBinding

class PokeUserListAdapter(
    private val pokeUserListItemViewType: PokeUserListItemViewType,
    private val clickListener: PokeUserListClickListener,
) : ListAdapter<PokeUser, PokeUserViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new ->
            old.userId == new.userId
                    && old.isAlreadyPoke == new.isAlreadyPoke
                    && old.pokeNum == new.pokeNum
        },
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeUserViewHolder {
        return when (pokeUserListItemViewType) {
            PokeUserListItemViewType.SMALL -> PokeUserViewHolder.Small(
                ItemPokeUserSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> PokeUserViewHolder.Large(
                ItemPokeUserLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: PokeUserViewHolder, position: Int) {
        holder.apply {
            onBind(currentList[holder.adapterPosition])
            itemView.findViewById<ImageView>(R.id.imageView_profile).setOnClickListener {
                clickListener.onClickProfileImage(currentList[holder.adapterPosition].playgroundId)
            }
            itemView.findViewById<ImageButton>(R.id.imageButton_poke).setOnClickListener {
                if (currentList[holder.adapterPosition].isAlreadyPoke) return@setOnClickListener
                clickListener.onClickPokeButton(currentList[holder.adapterPosition])
            }
        }
    }
}
