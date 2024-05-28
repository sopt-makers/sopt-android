package org.sopt.official.feature.poke.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.parcelize.Parcelize
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.FragmentOnboardingFriendsBinding
import org.sopt.official.feature.poke.user.PokeUserListAdapter
import org.sopt.official.feature.poke.user.PokeUserListClickListener
import org.sopt.official.feature.poke.user.PokeUserListItemViewType

class OnboardingPokeUserFragment : Fragment() {
    private var _binding: FragmentOnboardingFriendsBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "ERROR" }

    private val pokeUserListAdapter
        get() = binding.recyclerView.adapter as PokeUserListAdapter?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnboardingFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokeUser = arguments?.getParcelableArrayCompat<PokeUserParcelable>(ARG_PROFILES)
        binding.recyclerView.adapter = PokeUserListAdapter(
            pokeUserListItemViewType = PokeUserListItemViewType.LARGE,
            clickListener = pokeUserListClickLister,
        )
        Log.e("TAG", "pokeUser : $pokeUser", )
        updateRecyclerView(pokeUser?.toList().orEmpty())
    }

    private fun updateRecyclerView(data: List<PokeUserParcelable>) {
        Log.e("TAG", "data : $data", )
        pokeUserListAdapter?.submitList(data.map { it.toPokeUser() })
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private val pokeUserListClickLister =
        object : PokeUserListClickListener {
            override fun onClickProfileImage(playgroundId: Int) {
//                tracker.track(
//                    type = EventType.CLICK,
//                    name = "memberprofile",
//                    properties = mapOf("view_type" to args?.userStatus, "click_view_type" to "onboarding", "view_profile" to playgroundId),
//                )
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
            }

            override fun onClickPokeButton(user: PokeUser) {
//                tracker.track(
//                    type = EventType.CLICK,
//                    name = "poke_icon",
//                    properties =
//                    mapOf(
//                        "view_type" to args?.userStatus,
//                        "click_view_type" to "onboarding",
//                        "view_profile" to user.playgroundId,
//                    ),
//                )

//                messageListBottomSheet =
//                    MessageListBottomSheetFragment.Builder()
//                        .setMessageListType(PokeMessageType.POKE_SOMEONE)
//                        .onClickMessageListItem { message -> viewModel.pokeUser(user.userId, message) }
//                        .create()
//
//                messageListBottomSheet?.let {
//                    it.show(supportFragmentManager, it.tag)
//                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PROFILES = "profiles"

        fun newInstance(pokeUsers: List<PokeUserParcelable>) = OnboardingPokeUserFragment().apply {
            arguments = Bundle().apply {
                putParcelableArray(ARG_PROFILES, pokeUsers.toTypedArray())
            }
        }
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String): Array<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArray(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableArray(key)?.filterIsInstance<T>()?.toTypedArray()
    }
}

@Parcelize
data class PokeUserParcelable(
    val userId: Int,
    val playgroundId: Int,
    val profileImage: String,
    val name: String,
    val message: String,
    val generation: Int,
    val part: String,
    val pokeNum: Int,
    val relationName: String,
    val mutualRelationMessage: String,
    val isFirstMeet: Boolean,
    var isAlreadyPoke: Boolean,
) : Parcelable {
    fun toPokeUser() = PokeUser(
        userId = userId,
        playgroundId = playgroundId,
        profileImage = profileImage,
        name = name,
        message = message,
        generation = generation,
        part = part,
        pokeNum = pokeNum,
        relationName = relationName,
        mutualRelationMessage = mutualRelationMessage,
        isFirstMeet = isFirstMeet,
        isAlreadyPoke = isAlreadyPoke,
    )
}

fun PokeUser.toParcelable() = PokeUserParcelable(
    userId = userId,
    playgroundId = playgroundId,
    profileImage = profileImage,
    name = name,
    message = message,
    generation = generation,
    part = part,
    pokeNum = pokeNum,
    relationName = relationName,
    mutualRelationMessage = mutualRelationMessage,
    isFirstMeet = isFirstMeet,
    isAlreadyPoke = isAlreadyPoke
)
