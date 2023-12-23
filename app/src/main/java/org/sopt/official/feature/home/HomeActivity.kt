/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.R
import org.sopt.official.analytics.AmplitudeTracker
import org.sopt.official.analytics.EventType
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.drawableOf
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.util.setOnSingleClickListener
import org.sopt.official.common.util.stringOf
import org.sopt.official.common.util.ui.setVisible
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivitySoptMainBinding
import org.sopt.official.databinding.ItemMainSmallBinding
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.feature.attendance.AttendanceActivity
import org.sopt.official.feature.home.adapter.SmallBlockAdapter
import org.sopt.official.feature.home.model.HomeCTAType
import org.sopt.official.feature.home.model.HomeMenuType
import org.sopt.official.feature.home.model.UserUiState
import org.sopt.official.feature.mypage.mypage.MyPageActivity
import org.sopt.official.feature.notification.AlertDialogOneButton
import org.sopt.official.feature.notification.NotificationHistoryActivity
import org.sopt.official.feature.notification.enums.DeepLinkType
import org.sopt.official.feature.poke.PokeMainActivity
import org.sopt.official.stamp.SoptampActivity
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySoptMainBinding::inflate)
    private val viewModel by viewModels<HomeViewModel>()
    private val args by serializableExtra(StartArgs(UserStatus.UNAUTHENTICATED))

    @Inject
    lateinit var tracker: AmplitudeTracker
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
        tracker.track(type = EventType.VIEW, name = "apphome", properties = mapOf("view_type" to args?.userStatus?.value))

        requestNotificationPermission()
        initToolbar()
        initUserStatus()
        initUserInfo()
        initBlock()

        handleDeepLinkDialog()
    }

    override fun onResume() {
        super.onResume()
        viewModel.initHomeUi(args?.userStatus ?: UserStatus.UNAUTHENTICATED)
    }

    private fun initUserStatus() {
        viewModel.initMainDescription(args?.userStatus ?: UserStatus.UNAUTHENTICATED)
        viewModel.registerPushToken(args?.userStatus ?: UserStatus.UNAUTHENTICATED)
    }

    private fun initToolbar() {
        binding.mypage.setOnClickListener {
            tracker.track(type = EventType.CLICK, name = "mypage", properties = mapOf("view_type" to args?.userStatus?.value))
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
                binding.contentPoke.root.setOnSingleClickListener {
                    if (userActiveState == UserActiveState.ACTIVE) {
                        startActivity(Intent(this, PokeMainActivity::class.java))
                    } else {
                        AlertDialogOneButton(this)
                            .setTitle(R.string.poke_dialog_preparing_title)
                            .setSubtitle(R.string.poke_dialog_preparing_body)
                            .setPositiveButton(R.string.poke_dialog_confirm_button)
                            .show()
                    }
                }

                val isClickable = userActiveState != UserActiveState.UNAUTHENTICATED
                if (isClickable) {
                    val intent = Intent(this@HomeActivity, SoptampActivity::class.java)
                    binding.contentSoptamp.root.setOnSingleClickListener {
                        tracker.track(type = EventType.CLICK, name = "soptamp", properties = mapOf("view_type" to args?.userStatus?.value))
                        this@HomeActivity.startActivity(intent)
                    }
                }

                val isAuthenticated = userActiveState != UserActiveState.UNAUTHENTICATED
                binding.imageViewNotificationBadge.visibility = if (isAuthenticated) View.VISIBLE else View.GONE
                binding.imageViewNotificationHistory.visibility = if (isAuthenticated) View.VISIBLE else View.GONE
                binding.imageViewNotificationHistory.setOnClickListener {
                    tracker.track(type = EventType.CLICK, name = "alarm", properties = mapOf("view_type" to args?.userStatus?.value))
                    val intent = Intent(this, NotificationHistoryActivity::class.java)
                    startActivity(intent)
                }
            }.launchIn(lifecycleScope)
        viewModel.isAllNotificationsConfirm.flowWithLifecycle(lifecycle)
            .onEach {
                if (viewModel.userActiveState.value == UserActiveState.UNAUTHENTICATED) return@onEach
                binding.imageViewNotificationBadge.visibility = if (it) View.GONE else View.VISIBLE
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
        viewModel.mainMenuContents
            .flowWithLifecycle(lifecycle)
            .onEach { item ->
                item.let { (largeBlock, topSmallBlock, bottomSmallBlock) ->
                    setCTAContent(largeBlock)
                    setMainMenuContent(binding.smallBlock1, topSmallBlock)
                    setMainMenuContent(binding.smallBlock2, bottomSmallBlock)
                }
            }.launchIn(lifecycleScope)
        viewModel.subMenuContents
            .flowWithLifecycle(lifecycle)
            .onEach {
                smallBlockAdapter?.submitList(it)
            }.launchIn(lifecycleScope)
    }

    private fun setCTAContent(item: HomeCTAType) {
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
                tracker.track(type = EventType.CLICK, name = "attendance", properties = mapOf("view_type" to args?.userStatus?.value))
                startActivity(intent)
            }
        }
    }

    private fun setMainMenuContent(view: ItemMainSmallBinding, item: HomeMenuType) {
        with(view) {
            icon.background = drawableOf(item.icon)
            title.text = stringOf(item.title)
            descriptionSmall.isVisible = item.description != null
            descriptionSmall.text = item.description?.let { stringOf(it) }
            root.setOnSingleClickListener {
                tracker.track(
                    type = EventType.CLICK,
                    name = item.clickEventName,
                    properties = mapOf("view_type" to args?.userStatus?.value)
                )
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                startActivity(intent)
            }
        }
    }

    private fun handleDeepLinkDialog() {
        if (
            args?.deepLinkType != DeepLinkType.UNKNOWN
            && args?.deepLinkType != DeepLinkType.EXPIRED
        ) return

        val titleRes = when (args?.deepLinkType == DeepLinkType.UNKNOWN) {
            true -> R.string.notification_dialog_unknown_title
            false -> R.string.notification_dialog_expired_title
        }

        val bodyRes = when (args?.deepLinkType == DeepLinkType.UNKNOWN) {
            true -> R.string.notification_dialog_unknown_body
            false -> R.string.notification_dialog_expired_body
        }

        AlertDialogOneButton(this)
            .setTitle(titleRes)
            .setSubtitle(bodyRes)
            .setPositiveButton(R.string.notification_dialog_confirm_button)
            .show()
    }

    data class StartArgs(
        val userStatus: UserStatus,
        val deepLinkType: DeepLinkType? = null,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, HomeActivity::class.java).apply {
                putExtra("args", args)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
    }
}
