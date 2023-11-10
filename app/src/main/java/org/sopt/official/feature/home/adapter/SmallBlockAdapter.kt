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
package org.sopt.official.feature.home.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.databinding.ItemMainSmallBlockListBinding
import org.sopt.official.feature.home.model.HomeMenuType
import org.sopt.official.feature.mypage.util.drawableOf
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.feature.mypage.util.stringOf

class SmallBlockAdapter : ListAdapter<HomeMenuType, SmallBlockAdapter.ViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.title == new.title },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    class ViewHolder(
        private val binding: ItemMainSmallBlockListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: HomeMenuType) {
            val context = binding.root.context
            with(binding.itemView) {
                icon.background = context.drawableOf(item.icon)
                title.text = context.stringOf(item.title)
                descriptionSmall.isVisible = item.description != null
                descriptionSmall.text = item.description?.let { context.stringOf(it) }
                root.setOnSingleClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemMainSmallBlockListBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
