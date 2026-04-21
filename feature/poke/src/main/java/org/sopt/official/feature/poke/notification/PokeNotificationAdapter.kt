package org.sopt.official.feature.poke.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.official.common.view.ItemDiffCallback
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ItemPokeNotificationBinding
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.util.setRelationStrokeColor

class PokeNotificationAdapter(
    private val clickListener: PokeUserListClickListener,
) : ListAdapter<PokeUser, PokeNotificationAdapter.NotificationListViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old.userId == new.userId },
        onItemsTheSame = { old, new -> old == new },
    ),
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListViewHolder {
        return NotificationListViewHolder(ItemPokeNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationListViewHolder, position: Int) {
        holder.apply {
            onBind(currentList[position])

            itemView.findViewById<ImageView>(R.id.img_user_profile).setOnClickListener {
                if (currentList[position].isAnonymous) return@setOnClickListener
                clickListener.onClickProfileImage(currentList[position].userId)
            }

            itemView.findViewById<ImageView>(R.id.img_poke).setOnClickListener {
                if (currentList[position].isAlreadyPoke) return@setOnClickListener
                clickListener.onClickPokeButton(currentList[position])
            }
        }
    }

    fun updatePokeUserItemPokeState(userId: Int) {
        val newList = currentList.toMutableList()
        val pokeUser = newList.find { it.userId == userId }
        val position = newList.indexOf(pokeUser)

        pokeUser?.isAlreadyPoke = true
        submitList(newList)
        notifyItemChanged(position)
    }

    fun updatePokeNotification(newList: List<PokeUser>) {
        submitList(newList)
        notifyDataSetChanged()
    }

    inner class NotificationListViewHolder(
        private val viewBinding: ItemPokeNotificationBinding,
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun onBind(item: PokeUser) {
            with(viewBinding) {
                if (item.isAnonymous) {
                    val anonymousImage = item.anonymousImage.trim()
                    val hasValidAnonymousImage =
                        anonymousImage.isNotBlank() &&
                            !anonymousImage.equals("null", ignoreCase = true) &&
                            !anonymousImage.equals("nullanonymous.png", ignoreCase = true) &&
                            !anonymousImage.contains("nullanonymous", ignoreCase = true)

                    if (hasValidAnonymousImage) {
                        imgUserProfile.load(anonymousImage) {
                            placeholder(R.drawable.ic_empty_profile)
                            error(R.drawable.ic_empty_profile)
                            fallback(R.drawable.ic_empty_profile)
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        imgUserProfile.setImageResource(R.drawable.ic_empty_profile)
                    }

                    tvUserName.text = item.anonymousName
                    tvUserGeneration.visibility = View.GONE
                    tvUserFriendsStatus.visibility = View.GONE
                } else {
                    val profileImage = item.profileImage.trim()

                    if (profileImage.isNotBlank()) {
                        imgUserProfile.load(profileImage) {
                            placeholder(R.drawable.ic_empty_profile)
                            error(R.drawable.ic_empty_profile)
                            fallback(R.drawable.ic_empty_profile)
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        imgUserProfile.setImageResource(R.drawable.ic_empty_profile)
                    }

                    tvUserName.text = item.name
                    tvUserGeneration.text = root.context.getString(R.string.poke_user_info, item.generation, item.part)
                    tvUserFriendsStatus.text =
                        if (item.isFirstMeet) {
                            item.mutualRelationMessage
                        } else {
                            "친한친구 ${item.pokeNum}콕"
                        }
                }
                imgUserProfileOutline.setRelationStrokeColor(item.relationName)
                tvUserMessage.text = item.message
                imgPoke.isEnabled = !item.isAlreadyPoke
            }
        }
    }
}