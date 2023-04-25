package org.sopt.official.feature.mypage.signOut

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.databinding.ActivitySignOutBinding
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class SignOutActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySignOutBinding::inflate)
    private val viewModel by viewModels<SignOutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initClick()
        initRestart()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initClick() {
        binding.confirmButton.setOnClickListener {
            viewModel.signOut()
            this.finish()
        }
    }

    private fun initRestart() {
        viewModel.restartSignal
            .flowWithLifecycle(lifecycle)
            .onEach { ProcessPhoenix.triggerRebirth(this) }
            .launchIn(lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignOutActivity::class.java)
    }
}