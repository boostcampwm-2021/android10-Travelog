package com.thequietz.travelog.schedule.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding
import com.thequietz.travelog.schedule.Schedule
import com.thequietz.travelog.schedule.ScheduleViewHolder

class ScheduleRecyclerAdapter : ListAdapter<Schedule, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleViewHolder(
            ItemRecyclerScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val schedule = getItem(position)
        (holder as ScheduleViewHolder).bind(schedule)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule) =
                oldItem == newItem
        }
    }
}
