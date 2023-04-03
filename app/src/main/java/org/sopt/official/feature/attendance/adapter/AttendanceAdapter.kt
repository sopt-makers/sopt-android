package org.sopt.official.feature.attendance.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AttendanceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    companion object {
        private const val USER_INFO = 0
        private const val SUMMARY = 1
        private const val LOG_HEADER = 2
        private const val LOG = 3
    }
}