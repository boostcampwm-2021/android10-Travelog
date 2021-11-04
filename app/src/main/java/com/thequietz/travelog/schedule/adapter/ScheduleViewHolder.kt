package com.thequietz.travelog.schedule.adapter

import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding
import com.thequietz.travelog.schedule.model.ScheduleModel

class ScheduleViewHolder(
    private val binding: ItemRecyclerScheduleBinding
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            // TODO: 세부 일정 설정 화면으로 이동
        }
    }

    fun bind(item: ScheduleModel) {
        binding.apply {
            schedule = item
            executePendingBindings()
        }
    }
}
