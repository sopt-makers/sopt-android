/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeUserLargeBinding
import org.sopt.official.feature.poke.databinding.ItemPokeUserSmallBinding
import org.sopt.official.feature.poke.util.setRelationStrokeColor

sealed class PokeUserViewHolder(
    binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(pokeUser: PokeUser)

    class Small(
        private val binding: ItemPokeUserSmallBinding,
    ) : PokeUserViewHolder(binding) {
        override fun onBind(pokeUser: PokeUser) {
            binding.apply {
                imageViewFriendRelationOutline.setRelationStrokeColor(pokeUser.relationName)
                if (pokeUser.isAnonymous) {
                    imageViewProfile.load(pokeUser.anonymousImage) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                    textViewUserName.text = pokeUser.anonymousName
                    textViewUserInfo.isVisible = false
                } else {
                    pokeUser.profileImage.takeIf { it.isNotEmpty() }?.let {
                        imageViewProfile.load(it) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    } ?: imageViewProfile.setImageResource(R.drawable.ic_empty_profile)
                    textViewUserName.text = pokeUser.name
                    textViewUserInfo.text = root.context.getString(R.string.poke_user_info, pokeUser.generation, pokeUser.part)
                }
                textViewPokeCount.text = root.context.getString(R.string.poke_user_poke_count, pokeUser.pokeNum)
                imageButtonPoke.isEnabled = !pokeUser.isAlreadyPoke
            }
        }
    }

    class Large(
        private val binding: ItemPokeUserLargeBinding,
    ) : PokeUserViewHolder(binding) {
        override fun onBind(pokeUser: PokeUser) {
            binding.apply {
                when (pokeUser.profileImage.isBlank()) {
                    true ->
                        imageViewProfile.load(null) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    false ->
                        imageViewProfile.load(pokeUser.profileImage) {
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
