package org.sopt.official.feature.home.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.sopt.official.core.view.ItemDiffCallback
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
