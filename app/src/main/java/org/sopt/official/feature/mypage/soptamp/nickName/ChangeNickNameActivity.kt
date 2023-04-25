package org.sopt.official.feature.mypage.soptamp.nickName

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.databinding.ActivityChangeNickNameBinding
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class ChangeNickNameActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityChangeNickNameBinding::inflate)
    private val viewModel by viewModels<ChangeNickNameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initView()
        initAction()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
        viewModel.backPressedSignal
            .flowWithLifecycle(lifecycle)
            .filter { it }
            .onEach {
                this.onBackPressedDispatcher.onBackPressed()
            }
            .launchIn(lifecycleScope)
        viewModel.isValidNickName
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.errorMessage.setVisible(!it)
                binding.edittext.background = this.getDrawable(
                    if (it) R.drawable.layout_edit_text_background
                    else R.drawable.layout_edit_text_error_background
                )
            }
            .launchIn(lifecycleScope)
    }

    private fun initAction() {
        binding.confirmButton.setOnClickListener {
            viewModel.changeNickName()
        }
        binding.edittext.addTextChangedListener {
            viewModel.nickName.value = it.toString()
            binding.confirmButton.isEnabled = (it?.length ?: 0) > 0
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, ChangeNickNameActivity::class.java).apply {
        }
    }
}