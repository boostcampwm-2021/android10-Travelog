package com.thequietz.travelog.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding
import com.thequietz.travelog.ui.schedule.model.ScheduleModel

class ScheduleRecyclerAdapter(
    val onClick: (ScheduleModel) -> (Unit),
    val onDelete: (Int) -> (Unit)
) : ListAdapter<ScheduleModel, RecyclerView.ViewHolder>(ScheduleRecyclerDiffUtil()) {

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
}

private class ScheduleRecyclerDiffUtil : DiffUtil.ItemCallback<ScheduleModel>() {
    override fun areItemsTheSame(oldItem: ScheduleModel, newItem: ScheduleModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ScheduleModel, newItem: ScheduleModel) =
        oldItem == newItem
}
