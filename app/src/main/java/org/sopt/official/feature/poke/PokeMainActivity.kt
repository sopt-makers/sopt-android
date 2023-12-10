package org.sopt.official.feature.poke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivityPokeMainBinding

@AndroidEntryPoint
class PokeMainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityPokeMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scrollviewPokeMain.viewTreeObserver.addOnScrollChangedListener {
            binding.refreshLayoutPokeMain.isEnabled = binding.scrollviewPokeMain.scrollY == 0
        }
    }
}