package org.sopt.official.feature.mypage.soptamp.sentence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.sopt.official.databinding.ActivityAdjustSentenceBinding
import org.sopt.official.util.rx.observeOnMain
import org.sopt.official.util.rx.subscribeBy
import org.sopt.official.util.rx.subscribeOnIo
import org.sopt.official.util.ui.throttleUi
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class AdjustSentenceActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAdjustSentenceBinding::inflate)
    private val viewModel by viewModels<AdjustSentenceViewModel>()

    private val createDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initView()
        initClick()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
        viewModel.backPressedSignal
            .toFlowable(BackpressureStrategy.LATEST)
            .filter { it }
            .subscribeOnIo()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(createDisposable,
                onNext = {
                    this.onBackPressedDispatcher.onBackPressed()
                })
        binding.edittext.textChanges()
            .throttleUi()
            .map { it.isNotEmpty() }
            .distinctUntilChanged()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(createDisposable,
                onNext = {
                    binding.confirmButton.isEnabled = it
                })
    }

    private fun initClick() {
        binding.confirmButton.clicks()
            .throttleUi()
            .map { binding.edittext.text.toString() }
            .distinctUntilChanged()
            .onBackpressureLatest()
            .subscribeBy(createDisposable,
                onNext = {
                    viewModel.adjustSentence(it)
                })
    }

    override fun onDestroy() {
        super.onDestroy()

        createDisposable.dispose()
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java).apply {
        }
    }
}