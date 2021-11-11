package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerOtherInfoBinding
import com.thequietz.travelog.guide.RecommendPlace

class OtherInfoAdapter : androidx.recyclerview.widget.ListAdapter<RecommendPlace, OtherInfoAdapter.OtherInfoViewHolder>(
    diffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherInfoViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_other_info,
            parent,
            false
        ) as ItemRecyclerOtherInfoBinding
        return OtherInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OtherInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OtherInfoViewHolder(val binding: ItemRecyclerOtherInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecommendPlace>() {
            override fun areItemsTheSame(
                oldItem: RecommendPlace,
                newItem: RecommendPlace
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RecommendPlace,
                newItem: RecommendPlace
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
