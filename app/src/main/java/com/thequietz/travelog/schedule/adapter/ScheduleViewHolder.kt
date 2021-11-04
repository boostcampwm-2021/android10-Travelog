package com.thequietz.travelog.schedule.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding
import com.thequietz.travelog.schedule.model.ScheduleModel

class ScheduleViewHolder(
    private val binding: ItemRecyclerScheduleBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var id: Int? = null

    init {
        binding.apply {
            setClickListener {
                val adapter = bindingAdapter as ScheduleRecyclerAdapter
                if (btnRemove.visibility == View.VISIBLE)
                    btnRemove.visibility = View.GONE
                else
                    id?.let { adapter.onClick(it) }
            }

            setLongClickListener {
                btnRemove.visibility = View.VISIBLE
                true
            }

            btnRemove.setOnClickListener {
                val adapter = bindingAdapter as ScheduleRecyclerAdapter
                id?.let { adapter.onDelete(it) }
            }
        }
    }

    fun bind(item: ScheduleModel) {
        binding.apply {
            schedule = item
            executePendingBindings()
        }
    }
}
