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
package org.sopt.official.feature.poke.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.feature.poke.databinding.ItemPokeSimilarFriendsBinding
import org.sopt.official.feature.poke.user.PokeUserListClickListener

class PokeMainListAdapter(
    private val clickListener: PokeUserListClickListener,
) : ListAdapter<PokeRandomUserList.PokeRandomUsers, PokeMainViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new ->
            old.userInfoList == new.userInfoList
        },
    ),
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeMainViewHolder {
        val binding = ItemPokeSimilarFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokeMainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokeMainViewHolder, position: Int) {
        with(holder) {
            onBind(currentList[position], clickListener)
        }
    }

    override fun onViewRecycled(holder: PokeMainViewHolder) {
        super.onViewRecycled(holder)
        holder.resetImage()
    }
}
