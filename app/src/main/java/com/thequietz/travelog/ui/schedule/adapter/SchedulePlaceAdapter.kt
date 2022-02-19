package com.thequietz.travelog.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerSchedulePlaceBinding
import com.thequietz.travelog.ui.schedule.model.SchedulePlaceModel

class SchedulePlaceAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<SchedulePlaceModel, SchedulePlaceAdapter.ViewHolder>(SchedulePlaceDiffUtil()) {
    class ViewHolder(val binding: ItemRecyclerSchedulePlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: SchedulePlaceModel, position: Int, listener: OnItemClickListener) {
            binding.model = model
            binding.tvGuideItem.text = model.cityName
            binding.ibGuideItem.setOnClickListener { v ->
                when (model.isSelected) {
                    true -> binding.ibGuideItem.alpha = 0.4F
                    false -> binding.ibGuideItem.alpha = 0.9F
                }
                model.isSelected = !model.isSelected
                listener.onItemClick(v, position, model.isSelected)
            }
            binding.ibGuideItem.let {
                Glide.with(it)
                    .load(model.thumbnail)
                    .centerCrop()
                    .override(it.measuredWidth, it.measuredHeight)
                    .into(it)

                when (model.isSelected) {
                    true -> it.alpha = 0.9F
                    false -> it.alpha = 0.4F
                }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerSchedulePlaceBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_schedule_place,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, toggle: Boolean)
    }
}

private class SchedulePlaceDiffUtil : DiffUtil.ItemCallback<SchedulePlaceModel>() {
    override fun areItemsTheSame(oldItem: SchedulePlaceModel, newItem: SchedulePlaceModel): Boolean {
        return oldItem.cityName == newItem.cityName
    }

    override fun areContentsTheSame(oldItem: SchedulePlaceModel, newItem: SchedulePlaceModel): Boolean {
        return oldItem == newItem
    }
}
