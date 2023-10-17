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
import org.sopt.official.util.drawableOf
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.stringOf

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
