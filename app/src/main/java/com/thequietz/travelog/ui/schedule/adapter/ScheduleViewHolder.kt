package com.thequietz.travelog.ui.schedule.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemRecyclerScheduleBinding
import com.thequietz.travelog.ui.schedule.model.ScheduleModel

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
                    id?.let { adapter.onClick(schedule as ScheduleModel) }
            }

            setLongClickListener {
                btnRemove.visibility = View.VISIBLE
                true
            }

            btnRemove.setOnClickListener {
                val adapter = bindingAdapter as ScheduleRecyclerAdapter
                Log.d("Loaded Data", "ok")
                id?.let { adapter.onDelete(it) }
            }
        }
    }

    fun bind(item: ScheduleModel) {
        id = item.id
        binding.apply {
            schedule = item
            executePendingBindings()
        }
    }
}
