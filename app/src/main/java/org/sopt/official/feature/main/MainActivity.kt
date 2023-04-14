package org.sopt.official.feature.main

import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.base.BaseAdapter
import org.sopt.official.base.BaseViewHolder
import org.sopt.official.databinding.ActivitySoptMainBinding
import org.sopt.official.databinding.ItemMainContentBinding
import org.sopt.official.databinding.ItemMainSmallBinding
import org.sopt.official.feature.main.MainViewModel.SmallBlockItemHolder
import org.sopt.official.feature.main.MainViewModel.ContentItemHolder

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoptMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val smallBlockAdapter: SmallBlockAdapter?
        get() = binding.smallBlockList.adapter as? SmallBlockAdapter

    private val contentAdapter: ContentAdapter?
        get() = binding.recyclerContent.adapter as? ContentAdapter

    private inner class SmallBlockAdapter : BaseAdapter<SmallBlockItemHolder>() {
        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)) {
                is SmallBlockItemHolder.SmallBlock -> SmallBlockViewType.SMALL_BLOCK
            }.ordinal
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SmallBlockItemHolder, *, *> {
            return when (SmallBlockViewType.values()[viewType]) {
                SmallBlockViewType.SMALL_BLOCK -> SmallBlockViewHolder(parent)
            }
        }
    }

    private inner class SmallBlockViewHolder(parent: ViewGroup) :
        BaseViewHolder<SmallBlockItemHolder, SmallBlockItemHolder.SmallBlock, ItemMainSmallBinding>(
            parent,
            ItemMainSmallBinding::inflate
        ) {
        override fun onBind(item: SmallBlockItemHolder.SmallBlock, position: Int) {
            super.onBind(item, position)
            binding.icon.background = this@MainActivity.getDrawable(item.icon)
            binding.title.text = item.name
        }
    }

    private inner class ContentAdapter : BaseAdapter<ContentItemHolder>() {
        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)) {
                is ContentItemHolder.Content -> ContentViewType.CONTENT
            }.ordinal
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ContentItemHolder, *, *> {
            return when (ContentViewType.values()[viewType]) {
                ContentViewType.CONTENT -> ContentViewHolder(parent)
            }
        }
    }

    private inner class ContentViewHolder(parent: ViewGroup) :
        BaseViewHolder<ContentItemHolder, ContentItemHolder.Content, ItemMainContentBinding>(
            parent,
            ItemMainContentBinding::inflate
        ) {
        override fun onBind(item: ContentItemHolder.Content, position: Int) {
            super.onBind(item, position)
        }
    }

    private enum class SmallBlockViewType {
        SMALL_BLOCK
    }
    private enum class ContentViewType {
        CONTENT
    }
}