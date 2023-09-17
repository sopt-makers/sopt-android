package org.sopt.official.feature.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.base.BaseAdapter
import org.sopt.official.base.BaseViewHolder
import org.sopt.official.databinding.ActivitySoptMainBinding
import org.sopt.official.databinding.ItemMainSmallBinding
import org.sopt.official.databinding.ItemMainSmallBlockListBinding
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.attendance.AttendanceActivity
import org.sopt.official.feature.main.MainViewModel.SmallBlockItemHolder
import org.sopt.official.feature.mypage.MyPageActivity
import org.sopt.official.stamp.SoptampActivity
import org.sopt.official.util.drawableOf
import org.sopt.official.util.serializableExtra
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.stringOf
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.viewBinding
import org.sopt.official.util.wrapper.getOrEmpty
import java.io.Serializable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySoptMainBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()
    private val args by serializableExtra(StartArgs(UserStatus.UNAUTHENTICATED))

    private val smallBlockAdapter: SmallBlockAdapter?
        get() = binding.smallBlockList.adapter as? SmallBlockAdapter

    /*
    private val contentAdapter: ContentAdapter?
        get() = binding.recyclerContent.adapter as? ContentAdapter

     */

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        askNotificationPermission()
        initToolbar()
        initUserStatus()
        initUserInfo()
        initBlock()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initUserStatus() {
        viewModel.initMainView(args?.userStatus ?: UserStatus.UNAUTHENTICATED)
    }

    private fun initToolbar() {
        binding.mypage.setOnClickListener {
            lifecycleScope.launch {
                val args = viewModel.userState
                    .map { MyPageActivity.StartArgs(it.getOrElse(UserState.UNAUTHENTICATED)) }
                    .first()
                startActivity(MyPageActivity.getIntent(this@MainActivity, args))
            }
        }
    }

    private fun initUserInfo() {
        viewModel.title
            .flowWithLifecycle(lifecycle)
            .onEach { (id, arg1, arg2) ->
                binding.title.text = getStringExt(id, arg1, arg2)
            }.launchIn(lifecycleScope)
        viewModel.userState
            .flowWithLifecycle(lifecycle)
            .onEach {
                val userState = it.get() ?: UserState.UNAUTHENTICATED
                binding.subtitle.text = getStringExt(
                    if (userState == UserState.UNAUTHENTICATED) {
                        R.string.main_subtitle_non_member
                    } else {
                        R.string.main_subtitle_member
                    }
                )
                binding.tagMemberState.isEnabled = userState == UserState.ACTIVE
                val isClickable = userState != UserState.UNAUTHENTICATED
                if (isClickable) {
                    val intent = Intent(this@MainActivity, SoptampActivity::class.java)
                    binding.contentSoptamp.root.setOnSingleClickListener {
                        this@MainActivity.startActivity(intent)
                    }
                }
            }.launchIn(lifecycleScope)
        viewModel.generatedTagText
            .flowWithLifecycle(lifecycle)
            .onEach { (id, text) ->
                binding.tagMemberState.text = getStringExt(id, text)
            }.launchIn(lifecycleScope)
        viewModel.generationList
            .flowWithLifecycle(lifecycle)
            .onEach {
                val generationList: List<Long> = it.getOrEmpty()
                binding.memberGeneration.generation1.setVisible(generationList.size >= 2)
                if (generationList.size >= 2) binding.memberGeneration.generation1.text = generationList[1].toString()
                binding.memberGeneration.generation2.setVisible(generationList.size >= 3)
                if (generationList.size >= 3) binding.memberGeneration.generation2.text = generationList[2].toString()
                binding.memberGeneration.generation3.setVisible(generationList.size >= 4)
                if (generationList.size >= 4) binding.memberGeneration.generation3.text = generationList[3].toString()
                binding.memberGeneration.generation4.setVisible(generationList.size >= 5)
                if (generationList.size >= 5) binding.memberGeneration.generation4.text = generationList[4].toString()
                binding.memberGeneration.generation5.setVisible(generationList.size >= 6)
                if (generationList.size >= 6) binding.memberGeneration.generation5.text = generationList[5].toString()
                binding.memberGeneration.generationAddition.setVisible(generationList.size >= 7)
                val additionalGeneration = (generationList.size - 5).toString()
                if (generationList.size >= 7) {
                    binding.memberGeneration.generationAddition.text =
                        this@MainActivity.getStringExt(R.string.main_additional_generation, additionalGeneration)
                }
            }.launchIn(lifecycleScope)
    }

    private fun initBlock() {
        binding.smallBlockList.apply {
            adapter = SmallBlockAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        viewModel.blockItem
            .flowWithLifecycle(lifecycle)
            .onEach { item ->
                val blockItem = item.get()
                binding.largeBlock.root.isVisible = blockItem != null
                binding.smallBlock1.root.isVisible = blockItem != null
                binding.smallBlock2.root.isVisible = blockItem != null
                blockItem?.let { (largeBlock, topSmallBlock, bottomSmallBlock) ->
                    setLargeBlock(largeBlock)
                    setSmallBlock(binding.smallBlock1, topSmallBlock)
                    setSmallBlock(binding.smallBlock2, bottomSmallBlock)
                }
            }.launchIn(lifecycleScope)
        viewModel.blockList
            .flowWithLifecycle(lifecycle)
            .onEach {
                smallBlockAdapter?.submitList(it)
            }.launchIn(lifecycleScope)
    }

    private fun setLargeBlock(item: MainViewModel.LargeBlockType) {
        with(binding) {
            largeBlock.icon.background = drawableOf(item.icon)
            largeBlock.name.text = stringOf(item.title)
            largeBlock.description.text = item.description?.let { stringOf(it) }
            largeBlock.description.isVisible = item.description != null
            item.description?.let { description ->
                largeBlock.description.text = stringOf(description)
            }
            val intent = if (item.url == null) {
                Intent(this@MainActivity, AttendanceActivity::class.java)
            } else {
                Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            }
            largeBlock.root.setOnSingleClickListener {
                startActivity(intent)
            }
        }
    }

    private fun setSmallBlock(view: ItemMainSmallBinding, item: MainViewModel.SmallBlockType) {
        with(view) {
            icon.background = drawableOf(item.icon)
            title.text = stringOf(item.title)
            descriptionSmall.isVisible = item.description != null
            descriptionSmall.text = item.description?.let { stringOf(it) }
            root.setOnSingleClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                startActivity(intent)
            }
        }
    }

    private fun getStringExt(@StringRes id: Int, args1: String? = null, args2: String? = null): String {
        return when {
            args2 != null -> this.getString(id, args1, args2)
            args1 != null -> this.getString(id, args1)
            else -> this.getString(id)
        }
    }

    private inner class SmallBlockAdapter : BaseAdapter<SmallBlockItemHolder>() {
        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)) {
                is SmallBlockItemHolder.SmallBlock -> SmallBlockViewType.SMALL_BLOCK
            }.ordinal
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SmallBlockItemHolder, *, *> {
            return when (SmallBlockViewType.entries[viewType]) {
                SmallBlockViewType.SMALL_BLOCK -> SmallBlockViewHolder(parent)
            }
        }
    }

    private inner class SmallBlockViewHolder(parent: ViewGroup) :
        BaseViewHolder<SmallBlockItemHolder, SmallBlockItemHolder.SmallBlock, ItemMainSmallBlockListBinding>(
            parent,
            ItemMainSmallBlockListBinding::inflate
        ) {
        override fun onBind(item: SmallBlockItemHolder.SmallBlock, position: Int) {
            super.onBind(item, position)
            setSmallBlock(binding.itemView, item.item)
        }
    }

    /* TODO: 나중에 진짜 recyclerView 사용해서

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
    */

    private enum class SmallBlockViewType {
        SMALL_BLOCK
    }

    data class StartArgs(
        val userStatus: UserStatus
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, MainActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}