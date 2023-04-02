package org.sopt.official.feature.attendance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.sopt.official.R
import org.sopt.official.databinding.ActivityAttendanceBinding

class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        binding.toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { this@AttendanceActivity.finish() }
        }
    }

    private fun initRecyclerView() {

    }
}