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
package org.sopt.official.feature.poke.main

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeSimilarFriendsBinding
import org.sopt.official.feature.poke.databinding.ItemPokeUserLargeBinding
import org.sopt.official.feature.poke.user.PokeUserListClickListener

class PokeMainViewHolder(
    private val binding: ItemPokeSimilarFriendsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(pokeUser: PokeRandomUserList.PokeRandomUsers, clickListener: PokeUserListClickListener) {
        with(binding) {
            tvRandomTitle.text = pokeUser.randomTitle

            when (pokeUser.userInfoList.size) {
                0 -> {
                    itemPokeUserLargeFirst.root.isVisible = false
                    itemPokeUserLargeSecond.root.isVisible = false
                }

                1 -> {
                    itemPokeUserLargeSecond.root.isVisible = false

                    val user = pokeUser.userInfoList[0]
                    bindUiData(itemPokeUserLargeFirst, user, clickListener)
                }

                else -> {
                    val firstUser = pokeUser.userInfoList[0]
                    bindUiData(itemPokeUserLargeFirst, firstUser, clickListener)

                    val secondUser = pokeUser.userInfoList[1]
                    bindUiData(itemPokeUserLargeSecond, secondUser, clickListener)
                }
            }
        }
    }

    private fun bindUiData(bind: ItemPokeUserLargeBinding, user: PokeUser, clickListener: PokeUserListClickListener) {
        with(bind) {
            user.profileImage.takeIf { it.isNotEmpty() }?.let {
                imageViewProfile.load(it) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            } ?: imageViewProfile.setImageResource(R.drawable.ic_empty_profile)

            textViewUserName.text = user.name
            textViewUserInfo.text = root.context.getString(R.string.poke_user_info, user.generation, user.part)

            imageButtonPoke.isEnabled = !user.isAlreadyPoke
            imageButtonPoke.setOnClickListener {
                if (user.isAlreadyPoke) return@setOnClickListener
                clickListener.onClickPokeButton(user)
            }
            imageViewProfile.setOnClickListener {
                clickListener.onClickProfileImage(user.playgroundId)
            }

        }
    }
}
