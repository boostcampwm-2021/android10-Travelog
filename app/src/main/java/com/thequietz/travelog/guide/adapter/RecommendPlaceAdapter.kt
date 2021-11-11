package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.ItemRecyclerRecommendPlaceBinding
import com.thequietz.travelog.guide.RecommendPlace

class RecommendPlaceAdapter : ListAdapter<RecommendPlace, RecommendPlaceAdapter.RecommendPlaceViewHolder>(
    diffUtil
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendPlaceViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recycler_recommend_place,
            parent,
            false
        ) as ItemRecyclerRecommendPlaceBinding
        return RecommendPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecommendPlaceViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class RecommendPlaceViewHolder(val binding: ItemRecyclerRecommendPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace?) {
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
