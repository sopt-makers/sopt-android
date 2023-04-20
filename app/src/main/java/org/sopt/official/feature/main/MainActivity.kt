package org.sopt.official.feature.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
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
import org.sopt.official.stamp.SoptampActivity
import org.sopt.official.util.serializableExtra
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initStartArgs()

        initUserInfo()
        initBlock()
        initContent()
    }

    private fun initStartArgs() {
        viewModel.initMainView(args?.userStatus ?: UserStatus.UNAUTHENTICATED)
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
                    if (userState == UserState.UNAUTHENTICATED) R.string.main_subtitle_non_member
                    else R.string.main_subtitle_member
                )
            }.launchIn(lifecycleScope)
        viewModel.userState
            .flowWithLifecycle(lifecycle)
            .onEach {
                val userState = it.get() ?: UserState.UNAUTHENTICATED
                binding.subtitle.text = getStringExt(
                    if (userState == UserState.UNAUTHENTICATED) R.string.main_subtitle_non_member
                    else R.string.main_subtitle_member
                )
                val state = it.getOrElse(UserState.UNAUTHENTICATED)
                binding.tagMemberState.isEnabled = state == UserState.ACTIVE
            }.launchIn(lifecycleScope)
        viewModel.generatedTagText
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.tagMemberState.text = getStringExt(it.first, it.second)
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
                if (generationList.size >= 7) binding.memberGeneration.generationAddition.text =
                    this@MainActivity.getStringExt(R.string.main_additional_generation, additionalGeneration)
            }.launchIn(lifecycleScope)
    }

    private fun initBlock() {
        binding.smallBlockList.apply {
            adapter = SmallBlockAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.blockItem.collect { item ->
                        val blockItem = item.get()
                        binding.largeBlock.root.setVisible(blockItem != null)
                        binding.smallBlock1.root.setVisible(blockItem != null)
                        binding.smallBlock2.root.setVisible(blockItem != null)
                        blockItem?.let {
                            setLargeBlock(it.first)
                            setSmallBlock(binding.smallBlock1, it.second)
                            setSmallBlock(binding.smallBlock2, it.third)
                        }
                    }
                }
                launch {
                    viewModel.blockList.collect {
                        smallBlockAdapter?.submitList(it)
                    }
                }
            }
        }
    }

    private fun initContent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userState.collect { userState ->
                        val isClickable = userState.get()?.let {
                            it != UserState.UNAUTHENTICATED
                        } ?: false
                        if (isClickable) {
                            val intent = Intent(this@MainActivity, SoptampActivity::class.java)
                            binding.contentSoptamp.root.setOnClickListener {
                                CoroutineScope(Dispatchers.Main).launch {
                                    this@MainActivity.startActivity(intent)
                                    delay(UI_THROTTLE_TIME)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setLargeBlock(item: MainViewModel.LargeBlockType) {
        binding.largeBlock.icon.background = this.getDrawable(item.icon)
        binding.largeBlock.name.text = this.getText(item.title)
        binding.largeBlock.description.setVisible(item.description != null)
        item.description?.let { description ->
            binding.largeBlock.description.text = this.getText(description)
        }
        val intent =
            if (item.url == null) Intent(this@MainActivity, AttendanceActivity::class.java)
            else Intent(Intent.ACTION_VIEW, Uri.parse(item.url))

        binding.largeBlock.root.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                this@MainActivity.startActivity(intent)
                delay(UI_THROTTLE_TIME)
            }
        }

    }

    private fun setSmallBlock(view: ItemMainSmallBinding, item: MainViewModel.SmallBlockType) {
        view.icon.background = this.getDrawable(item.icon)
        view.title.text = this.getText(item.title)
        view.root.setOnClickListener {
            lifecycleScope.launch {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                this@MainActivity.startActivity(intent)
                delay(UI_THROTTLE_TIME)
            }
        }
    }

    private fun getStringExt(id: Int, args1: String? = null, args2: String? = null): String {
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
            return when (SmallBlockViewType.values()[viewType]) {
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

    /*
    private enum class ContentViewType {
        CONTENT
    }
     */

    data class StartArgs(
        val userStatus: UserStatus
    ) : Serializable

    companion object {
        private const val UI_THROTTLE_TIME = 1000L

        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("args", args)
        }
    }
}