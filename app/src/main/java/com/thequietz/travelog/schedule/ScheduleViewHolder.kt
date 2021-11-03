package com.thequietz.travelog.schedule

import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding

class ScheduleViewHolder(
    private val binding: ItemRecyclerScheduleBinding
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            // TODO: 세부 일정 설정 화면으로 이동
        }
    }

    fun bind(item: Schedule) {
        binding.apply {
            schedule = item
            executePendingBindings()
        }
    }
}
