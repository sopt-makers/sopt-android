package org.sopt.official.feature.mypage.signOut

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.databinding.ActivitySignOutBinding
import org.sopt.official.util.rx.observeOnMain
import org.sopt.official.util.rx.subscribeBy
import org.sopt.official.util.rx.subscribeOnIo
import org.sopt.official.util.ui.throttleUi
import org.sopt.official.util.viewBinding
import org.sopt.official.util.wrapper.asNullableWrapper

@AndroidEntryPoint
class SignOutActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySignOutBinding::inflate)
    private val viewModel by viewModels<SignOutViewModel>()

    private val createDisposable = CompositeDisposable()

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
        binding.confirmButton.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(createDisposable,
                onNext = {
                    viewModel.signOut()
                    this.finish()
                })
    }

    private fun initRestart() {
        viewModel.restartSignal
            .distinctUntilChanged()
            .filter { it }
            .subscribeOnIo()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(createDisposable,
                onNext = {
                    val intent = packageManager.getLaunchIntentForPackage(packageName)
                    val componentName = intent?.component
                    val mainIntent = Intent.makeRestartActivityTask(componentName)
                    startActivity(mainIntent)
                    System.exit(0)
                })
    }

    override fun onDestroy() {
        super.onDestroy()

        createDisposable.dispose()
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignOutActivity::class.java).apply {
        }
    }
}