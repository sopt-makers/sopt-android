package org.sopt.official.feature.poke.recycler_view

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
        onContentsTheSame = { old, new -> old.userId == new.userId },
        onItemsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeUserViewHolder {
        return when (pokeUserListItemViewType) {
            PokeUserListItemViewType.SMALL -> PokeUserViewHolder.Small(
                ItemPokeUserSmallBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> PokeUserViewHolder.Large(
                ItemPokeUserLargeBinding.inflate(LayoutInflater.from(parent.context))
            )
        }
    }

    override fun onBindViewHolder(holder: PokeUserViewHolder, position: Int) {
        holder.apply {
            onBind(currentList[position])
            itemView.findViewById<ImageView>(R.id.imageView_profile).setOnClickListener {
                clickListener.onClickProfileImage()
            }
            itemView.findViewById<ImageButton>(R.id.imageButton_poke).setOnClickListener {
                clickListener.onClickPokeButton(currentList[position].userId)
            }
        }
    }
}