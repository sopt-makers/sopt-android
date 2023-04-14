package org.sopt.official.feature.main

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.databinding.ActivitySoptMainBinding
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoptMainBinding
    private val viewModel by viewModels<MainViewModel>()
}