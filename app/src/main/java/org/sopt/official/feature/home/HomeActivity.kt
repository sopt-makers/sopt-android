package org.sopt.official.feature.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.databinding.ActivitySoptMainBinding
import org.sopt.official.databinding.ItemMainSmallBinding
import org.sopt.official.domain.entity.UserActiveState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.feature.attendance.AttendanceActivity
import org.sopt.official.feature.home.adapter.SmallBlockAdapter
import org.sopt.official.feature.home.model.HomeCTAType
import org.sopt.official.feature.home.model.HomeMenuType
import org.sopt.official.feature.home.model.UserUiState
import org.sopt.official.feature.mypage.MyPageActivity
import org.sopt.official.stamp.SoptampActivity
import org.sopt.official.util.dp
import org.sopt.official.util.drawableOf
import org.sopt.official.util.serializableExtra
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.stringOf
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySoptMainBinding::inflate)
    private val viewModel by viewModels<HomeViewModel>()
    private val args by serializableExtra(UserStatus.UNAUTHENTICATED)

    private val smallBlockAdapter: SmallBlockAdapter?
        get() = binding.smallBlockList.adapter as? SmallBlockAdapter

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun requestNotificationPermission() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestNotificationPermission()
        initToolbar()
        initUserStatus()
        initUserInfo()
        initBlock()
    }

    private fun initUserStatus() {
        viewModel.initHomeUi(args ?: UserStatus.UNAUTHENTICATED)
        viewModel.initMainDescription(args ?: UserStatus.UNAUTHENTICATED)
    }

    private fun initToolbar() {
        binding.mypage.setOnClickListener {
            lifecycleScope.launch {
                startActivity(
                    MyPageActivity.getIntent(this@HomeActivity, MyPageActivity.StartArgs(viewModel.userActiveState.value))
                )
            }
        }
    }

    private fun initUserInfo() {
        viewModel.title
            .flowWithLifecycle(lifecycle)
            .onEach { user ->
                binding.title.text = when (user) {
                    is UserUiState.User -> {
                        val (id, userName, period) = user
                        stringOf(id, userName, period)
                    }

                    is UserUiState.InactiveUser -> stringOf(user.title)
                    is UserUiState.UnauthenticatedUser -> stringOf(user.title)
                }
            }.launchIn(lifecycleScope)
        viewModel.description
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.subtitle.text = it.topDescription.ifEmpty {
                    this@HomeActivity.getString(R.string.main_subtitle_non_member)
                }
                binding.bottomDescription.text = it.bottomDescription.ifEmpty {
                    this@HomeActivity.getString(R.string.main_content_header)
                }
            }.launchIn(lifecycleScope)
        viewModel.userActiveState
            .flowWithLifecycle(lifecycle)
            .onEach { userActiveState ->
                binding.tagMemberState.isEnabled = userActiveState == UserActiveState.ACTIVE
                val isClickable = userActiveState != UserActiveState.UNAUTHENTICATED
                if (isClickable) {
                    val intent = Intent(this@HomeActivity, SoptampActivity::class.java)
                    binding.contentSoptamp.root.setOnSingleClickListener {
                        this@HomeActivity.startActivity(intent)
                    }
                }
            }.launchIn(lifecycleScope)
        viewModel.generatedTagText
            .flowWithLifecycle(lifecycle)
            .onEach { (id, text) ->
                binding.tagMemberState.text = if (text == null) stringOf(id) else stringOf(id, text)
            }.launchIn(lifecycleScope)
        viewModel.generationList
            .flowWithLifecycle(lifecycle)
            .onEach { generationList ->
                binding.memberGeneration.generation1.setGenerationText(generationList, 2)
                binding.memberGeneration.generation2.setGenerationText(generationList, 3)
                binding.memberGeneration.generation3.setGenerationText(generationList, 4)
                binding.memberGeneration.generation4.setGenerationText(generationList, 5)
                binding.memberGeneration.generation5.setGenerationText(generationList, 6)
                binding.memberGeneration.generationAddition.setVisible(
                    generationList?.isLongerOrEqual(7) == true
                )
                val additionalGeneration = generationList?.length?.minus(5) ?: return@onEach
                if (generationList.isLongerOrEqual(7)) {
                    binding.memberGeneration.generationAddition.text = stringOf(
                        R.string.main_additional_generation,
                        additionalGeneration.toString()
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun TextView.setGenerationText(
        activeGeneration: SoptActiveGeneration?,
        comparator: Int
    ) {
        isVisible = activeGeneration?.isLongerOrEqual(comparator) == true
        if (activeGeneration?.isLongerOrEqual(comparator) == true) {
            text = activeGeneration.textAt(comparator - 1)
        }
    }

    private fun initBlock() {
        binding.smallBlockList.apply {
            adapter = SmallBlockAdapter()
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(0.dp, 0.dp, 12.dp, 0.dp)
                }
            })
            layoutManager = LinearLayoutManager(this@HomeActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        viewModel.blockItem
            .flowWithLifecycle(lifecycle)
            .onEach { item ->
                item.let { (largeBlock, topSmallBlock, bottomSmallBlock) ->
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

    private fun setLargeBlock(item: HomeCTAType) {
        with(binding) {
            largeBlock.icon.background = drawableOf(item.icon)
            largeBlock.name.text = stringOf(item.title)
            largeBlock.description.text = item.description?.let { stringOf(it) }
            largeBlock.description.isVisible = item.description != null
            item.description?.let { description ->
                largeBlock.description.text = stringOf(description)
            }
            val intent = if (item.url == null) {
                Intent(this@HomeActivity, AttendanceActivity::class.java)
            } else {
                Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            }
            largeBlock.root.setOnSingleClickListener {
                startActivity(intent)
            }
        }
    }

    private fun setSmallBlock(view: ItemMainSmallBinding, item: HomeMenuType) {
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

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: UserStatus) =
            Intent(context, HomeActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}
