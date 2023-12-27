package org.sopt.official.feature.poke.notification

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.common.util.viewBinding
import org.sopt.official.common.view.toast
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.databinding.ActivityPokeNotificationBinding
import org.sopt.official.feature.poke.main.PokeMainActivity
import org.sopt.official.feature.poke.message_bottom_sheet.MessageListBottomSheetFragment
import org.sopt.official.feature.poke.recycler_view.PokeUserListClickListener

@AndroidEntryPoint
class PokeNotificationActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeNotificationBinding::inflate)
    private val viewModel by viewModels<PokeNotificationViewModel>()

    private val pokeNotificationAdapter
        get() = binding.recyclerviewPokeNotification.adapter as PokeNotificationAdapter

    private val pokeNotificationLayoutManager
        get() = binding.recyclerviewPokeNotification.layoutManager as LinearLayoutManager

    private var messageListBottomSheet: MessageListBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        initListener()
        initRecyclerView()
        initStateFlowValues()
    }

    private fun initListener() {
        with(binding) {
            animationViewLottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    layoutLottie.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                    layoutLottie.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }
    }

    private val pokeUserListClickLister = object : PokeUserListClickListener {
        override fun onClickProfileImage(playgroundId: Int) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.poke_user_profile_url, playgroundId))))
        }

        override fun onClickPokeButton(userId: Int) {
            if (messageListBottomSheet?.isAdded == true) return
            if (messageListBottomSheet == null) {
                messageListBottomSheet = MessageListBottomSheetFragment.Builder()
                    .onClickMessageListItem { message -> viewModel.pokeUser(userId, message) }
                    .create()
            }

            messageListBottomSheet?.let {
                it.show(supportFragmentManager, it.tag)
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = pokeNotificationLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = pokeNotificationLayoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount % 10 == 0) {
                viewModel.getPokeNotification()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerviewPokeNotification) {
            adapter = PokeNotificationAdapter(pokeUserListClickLister)
            addOnScrollListener(scrollListener)
        }
    }

    private fun initStateFlowValues() {
        lifecycleScope.launch {
            viewModel.pokeNotification.collectLatest {
                pokeNotificationAdapter.updatePokeNotification(it?.history ?: arrayListOf())
            }
        }

        viewModel.pokeUserUiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Loading -> "Loading"
                    is UiState.Success<PokeUser> -> {
                        messageListBottomSheet?.dismiss()
                        if (it.data.isFirstMeet) {
                            messageListBottomSheet?.dismiss()
                            binding.tvLottie.text = binding.root.context.getString(R.string.friend_complete, it.data.name)
                            binding.animationViewLottie.playAnimation()
                        }
                    }
                    is UiState.ApiError -> if (it.responseMessage.isNotBlank()) toast(it.responseMessage)
                    is UiState.Failure -> it.throwable.message?.let { toast(it) }
                }
            }
            .launchIn(lifecycleScope)
    }
}