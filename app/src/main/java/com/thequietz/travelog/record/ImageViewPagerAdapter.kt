package com.thequietz.travelog.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemViewpagerImageBinding

class ImageViewPagerAdapter : ListAdapter<MyImage, ImageViewPagerAdapter.ViewPagerImageViewHolder>(
    diffUtil
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewPagerAdapter.ViewPagerImageViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_viewpager_image,
            parent,
            false
        ) as ItemViewpagerImageBinding
        return ViewPagerImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewPagerAdapter.ViewPagerImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewPagerImageViewHolder(val binding: ItemViewpagerImageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: MyImage) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyImage>() {
            override fun areItemsTheSame(oldItem: MyImage, newItem: MyImage): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MyImage, newItem: MyImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}