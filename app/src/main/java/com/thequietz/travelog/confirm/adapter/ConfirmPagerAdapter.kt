package com.thequietz.travelog.confirm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerConfirmPageBinding
import com.thequietz.travelog.schedule.model.ScheduleDetailModel

class ConfirmPagerAdapter :
    ListAdapter<ScheduleDetailModel, ConfirmPagerAdapter.ViewHolder>(ConfirmPagerDiffUtil()) {

    class ViewHolder(val binding: ItemRecyclerConfirmPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ScheduleDetailModel) {
            binding.model = model
            val imageUrl = model.destination.images.firstOrNull()?.imageUrl ?: model.place.thumbnail

            Glide.with(binding.ivConfirmPage)
                .load(imageUrl)
                .centerCrop()
                .into(binding.ivConfirmPage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecyclerConfirmPageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_confirm_page,
            parent,
            false
        )
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class ConfirmPagerDiffUtil : DiffUtil.ItemCallback<ScheduleDetailModel>() {
    override fun areItemsTheSame(
        oldItem: ScheduleDetailModel,
        newItem: ScheduleDetailModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ScheduleDetailModel,
        newItem: ScheduleDetailModel
    ): Boolean {
        return oldItem == newItem
    }
}
