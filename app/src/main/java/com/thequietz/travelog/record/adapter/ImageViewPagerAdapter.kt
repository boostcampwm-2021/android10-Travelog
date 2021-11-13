package com.thequietz.travelog.record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.databinding.ItemViewpagerImageBinding
import com.thequietz.travelog.record.model.RecordImage

class ImageViewPagerAdapter : ListAdapter<RecordImage, ImageViewPagerAdapter.ViewPagerImageViewHolder>(
    diffUtil
) {
    class ViewPagerImageViewHolder(val binding: ItemViewpagerImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecordImage) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ItemViewpagerImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecordImage>() {
            override fun areItemsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: RecordImage, newItem: RecordImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}
