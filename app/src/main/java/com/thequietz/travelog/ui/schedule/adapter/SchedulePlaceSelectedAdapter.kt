package com.thequietz.travelog.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSchedulePlaceSelectedBinding
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel

class SchedulePlaceSelectedAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<SchedulePlaceModel, SchedulePlaceSelectedAdapter.ViewHolder>(SchedulePlaceSelectedDiffUtil()) {

    class ViewHolder(val binding: ItemRecyclerSchedulePlaceSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: SchedulePlaceModel, position: Int, listener: OnItemClickListener) {
            binding.let {
                it.btnSelectedItem.text = model.cityName
                it.btnSelectedItem.setOnClickListener { v ->
                    listener.onItemClick(v, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerSchedulePlaceSelectedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_schedule_place_selected,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}

private class SchedulePlaceSelectedDiffUtil : DiffUtil.ItemCallback<SchedulePlaceModel>() {
    override fun areItemsTheSame(oldItem: SchedulePlaceModel, newItem: SchedulePlaceModel): Boolean {
        return oldItem.cityName == newItem.cityName
    }

    override fun areContentsTheSame(oldItem: SchedulePlaceModel, newItem: SchedulePlaceModel): Boolean {
        return oldItem == newItem
    }
}
