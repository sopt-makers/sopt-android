package org.sopt.official.feature.poke.common_recycler_view

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeUserLargeBinding
import org.sopt.official.feature.poke.databinding.ItemPokeUserSmallBinding
import org.sopt.official.feature.poke.util.getPokeFriendRelationColor

sealed class PokeUserViewHolder(
    binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(pokeUser: PokeUser)

    class Small(
        private val binding: ItemPokeUserSmallBinding,
    ) : PokeUserViewHolder(binding) {

        override fun onBind(pokeUser: PokeUser) {
            binding.apply {
                when (pokeUser.profileImage.isNullOrBlank()) {
                    true -> imageViewProfile.setImageResource(R.drawable.ic_empty_profile)
                    false -> imageViewProfile.load(pokeUser.profileImage) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                }
                imageViewFriendLevelIndicator.backgroundTintList = ContextCompat.getColorStateList(
                    root.context,
                    pokeUser.getPokeFriendRelationColor(),
                )
                textViewUserName.text = pokeUser.name
                textViewUserInfo.text = binding.root.context.getString(R.string.poke_user_info, pokeUser.generation, pokeUser.part)
                textViewPokeCount.text = pokeUser.pokeNum.toString()
                imageButtonPoke.isEnabled = !pokeUser.isAlreadyPoke
            }
        }
    }

    class Large(
        private val binding: ItemPokeUserLargeBinding,
    ) : PokeUserViewHolder(binding) {

        override fun onBind(pokeUser: PokeUser) {
            binding.apply {
                when (pokeUser.profileImage.isNullOrBlank()) {
                    true -> imageViewProfile.setImageResource(R.drawable.ic_empty_profile)
                    false -> imageViewProfile.load(pokeUser.profileImage) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                }
                textViewUserName.text = pokeUser.name
                textViewUserInfo.text = binding.root.context.getString(R.string.poke_user_info, pokeUser.generation, pokeUser.part)
                imageButtonPoke.isEnabled = !pokeUser.isAlreadyPoke
            }
        }
    }
}